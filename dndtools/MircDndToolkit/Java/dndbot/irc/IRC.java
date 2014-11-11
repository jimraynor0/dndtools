package dndbot.irc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.User;
import org.sheepy.pirc.PircListener;
import org.sheepy.pirc.PircSubject;
import org.sheepy.util.StringUtil;

import dndbot.module.BotModule;
import dndbot.module.Channel;
import dndbot.module.Core;
import dndbot.module.Database;
import dndbot.module.Dice;
import dndbot.module.Log;
import dndbot.module.ModuleEvent;
import dndbot.module.Time;
import dndbot.ui.PnlDiceBot;

/**
 * IRC Bot main class.  All commands are first parsed here.
 * Also handles automatic reconnect and store user / module data.
 *
 * Created by Ho Yiu YEUNG on Mar 8, 2007
 */
public class IRC extends PircSubject {
  public final Random rand;
  public static final String version = Colors.BOLD+"DnDBot "+Colors.BOLD+" 1.8.4";
  public static final String fullVersion = version + " GPL3 (c)2007-2011 by  Sheepy";

  /** Keep pinging server to detect disconnection */
  private Timer pingTimer;

  /** When true, auto reconnect on disconnect */
  public boolean reconnect;
  /** Target charset.  Unicode conversion happens automatically. */
  public String charset = "UTF8";

  /** Locale to use */
  public Locale locale = new Locale("en");
  /** Modules to use */
  public List<BotModule> mods = new ArrayList<BotModule>();

  /** Public command pattern */
  private Pattern commandPattern;
  /** Private command pattern */
  private Pattern directCommandPattern;
  /** Message breakdown pattern */
  private final Pattern crlfPattern = Pattern.compile("\\n");
  /** Leading IRC color and format, for adding command dot after colours  */
  private final Pattern leadingColorAndFormat = Pattern.compile("^\\s*((?:\\x02|\\x0F|\\x11|\\x12|\\x16|\\x1D|\\x1F|" + // Style commands: Bold, Color, TrueColor, Normal, Fixed, Reverse, ?, Italic, Underline
            "\\x03[\\dA-F]{0,2}(?:,[\\dA-F]{0,2})?|" + // mIRC colour
            "\\x04[\\dA-F]{6}(?:,(?:[\\dA-F]{6})?)?|" + // RGB colour
            "\\x04|" + StringUtil.ucSpace.pattern() + ")*)");
  /** Trailing IRC color and format, for removal before command detection */
  private final Pattern trailingColorAndFormat = Pattern.compile("(?:\\x02|\\x0F|\\x11|\\x12|\\x16|\\x1D|\\x1F|" + // Style commands: Bold, Color, TrueColor, Normal, Fixed, Reverse, ?, Italic, Underline
            "\\x03[\\dA-F]{0,2}(?:,[\\dA-F]{0,2})?|" + // mIRC colour
            "\\x04[\\dA-F]{6}(?:,(?:[\\dA-F]{6})?)?|" + // RGB colour
            "\\x04|" + StringUtil.ucSpace.pattern() + ")*$");
  /** Map of command to module */
  private final Map<String, BotModule> commandMap = new HashMap<String, BotModule>();

  /** DnDBot setting, stored by user and channel, system HashMap in ""  */
  public final Settings settings = new Settings();
  /** DnDBot data, used by module to store data that should survive a reset. */
  public final HashMap<String, Object> data = new HashMap<String, Object>();
  /** A map of joined channel's last command / log time */
  public static final Map<String, Long> active_channel = new HashMap<String, Long>();

  /** Module event triggered by processing event, on message only */
  public ModuleEvent event;

  /** Represent GUI user */
  public static final String USER_GUI = " (Bot owner) ";
  /** Admin password setting key */
  public static final String KEY_ADMIN_PASSWORD = "admin";

  public IRC() {
    super();
    init();
    rand = new Random();
  }

  public IRC(Random random, Locale locale) {
    super();
    this.locale = locale;
    init();
    rand = random;
  }

  private void init() {
    setName("DnDBot");
    reset();
  }

  public void addListener(PircListener listener) {
    super.addListener(listener);
  }

  public void removeListener(PircListener listener) {
    super.removeListener(listener);
  }

  /**
   * Recreate all modules, causing them to re-initialise
   */
  public void reset() {
    setMessageDelay(250);
    setVersion(fullVersion);
    for (BotModule m : mods) m.reset();
    mods.clear();
    try { mods.add(new Core(this)); } catch (Exception e) {e.printStackTrace();}
    try { mods.add(new Channel(this)); } catch (Exception e) {e.printStackTrace();}
    try { mods.add(new Dice(this)); } catch (Exception e) {e.printStackTrace();}
    try { mods.add(new Log(this)); } catch (Exception e) {e.printStackTrace();}
    //try { mods.add(new Database(this)); } catch (Exception e) {e.printStackTrace();}
    try { mods.add(new Time(this)); } catch (Exception e) {e.printStackTrace();}
    setModule(mods);
  }

  /**
   * Set modules and build command pattern
   * @param mods Mods to use
   */
  public void setModule(List<BotModule> mods) {
    this.mods = mods;
    commandMap.clear();
    StringBuilder pattern = new StringBuilder("(?<=^|\\s|:|\")" + // Start of line / space
            "(?:\\x02|\\x0F|\\x11|\\x12|\\x16|\\x1D|\\x1F|" + // Style commands: Bold, Color, TrueColor, Normal, Fixed, Reverse, ?, Italic, Underline
            "\\x03[\\dA-F]{0,2}(?:,[\\dA-F]{0,2})?|" + // mIRC colour
            "\\x04[\\dA-F]{6}(?:,(?:[\\dA-F]{6})?)?|" + // RGB colour
            "\\x04)*" + // Control code
            "\\.(");
    StringBuilder privatePattern = new StringBuilder(pattern);
    for (BotModule m : mods) {
      for (String s : m.getCommand()) {
        commandMap.put(s, m);
      }
      for (String s : m.getDirectCommand()) {
        if (!commandMap.containsKey(s))
          commandMap.put(s, m);
      }
      if (m.getCommandPattern() != null) pattern.append(m.getCommandPattern()).append('|');
      if (m.getDirectCommandPattern() != null) privatePattern.append(m.getDirectCommandPattern()).append('|');
    }
    pattern.setLength(Math.max(0, pattern.length() - 1));
    pattern.append(")(\\s.*|)$");
    privatePattern.setLength(Math.max(0, privatePattern.length() - 1));
    privatePattern.append(")(\\s.*?|)?$");
    commandPattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE);
    directCommandPattern = Pattern.compile(privatePattern.toString(), Pattern.CASE_INSENSITIVE);
  }

  static String lastAutoBlock;
  protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
    if (target.equalsIgnoreCase(getNick())) {
      if (notice.contains("Freebot") && notice.contains("已自动关闭") && notice.contains("自动小窗."))
        if (lastAutoBlock == null || !lastAutoBlock.equals(sourceNick)) {
          lastAutoBlock = sourceNick;
          sendNotice(sourceNick, "小窗讯息已被阻挡. 请关掉 Freebot 的自动保护");
        }
    }
    super.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
  }

  public void onMessage(String channel, String sender, String login, String hostname, String message) {
    ModuleEvent oldEvent = event;
    if (!sender.equalsIgnoreCase(getNick()))
      processMessage(message, channel, sender, login, hostname);
    super.onMessage(channel, sender, login, hostname, message);
    event = oldEvent;
  }

  public void onPrivateMessage(String sender, String login, String hostname, String message) {
    ModuleEvent oldEvent = event;
    String s = Colors.removeFormattingAndColors(message).trim();
    if (!s.startsWith("."))
        message = leadingColorAndFormat.matcher(message).replaceFirst("$1.");
    processPrivateMessage(message, sender, login, hostname);
    super.onPrivateMessage(sender, login, hostname, message);
    event = oldEvent;
  }

  /*
  protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
    // Process like private message
    ModuleEvent oldEvent = event;
    if (target.equalsIgnoreCase(getNick())) {
      if (!Colors.removeFormattingAndColors(notice).startsWith("."))
        notice = leadingColorAndFormat.matcher(notice).replaceFirst("$1.");
      checkCommand(notice, null, true, sourceNick, sourceLogin, sourceHostname, directCommandPattern);
    }
    super.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
    event = oldEvent;
  }
*/
  public void onAction(String sender, String login, String hostname, String target, String action) {
    super.onAction(sender, login, hostname, target, action);
  }

  /**
   * Updates settings (e.g. PC's "DM") on nick change
   * @param oldNick Old nickname
   * @param login User login
   * @param hostname User hostname
   * @param newNick New nickname
   */
  protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
    String old = oldNick.toLowerCase();
    // Change setting name
    if (settings.settings.containsKey(old))
      settings.settings.put(newNick.toLowerCase(), settings.settings.remove(old));
    // Change user's dm name
    if (!getNick().equalsIgnoreCase(newNick)) {
      for (Map.Entry<String, Map<String, String>> e : settings.settings.entrySet()) {
        if (e.getValue().containsKey("dm") && e.getValue().get("dm").equals(oldNick))
          e.getValue().put("dm", newNick);
      }
    }
  }

  /**
   * Detect and run command within messages
   * @param message Received message
   * @param channel Channel where message is received, maybe null
   * @param sender Nick of sender of the message
   * @param login Login of sender
   * @param hostname Hostname of sender
   */
  public void processMessage(String message, String channel, String sender, String login, String hostname) {
    checkCommand(message, channel, sender, login, hostname, false, commandPattern);
  }

  /**
   * Detect and run command within private messages
   * @param message Received message
   * @param sender Nick of sender of the message
   * @param login Login of sender
   * @param hostname Hostname of sender
   */
  public void processPrivateMessage(String message, String sender, String login, String hostname) {
    checkCommand(message, null, sender, login, hostname, false, directCommandPattern);
  }

  /**
   * Detect and run command within messages
   * @param message Received message
   * @param channel Channel where message is received, maybe null
   * @param sender Nick of sender of the message
   * @param login Login of sender
   * @param hostname Hostname of sender
   * @param isNotice Whether the command comes in form of notice
   * @param pattern Pattern used to find command
   */
  private void checkCommand(String message, String channel, String sender, String login, String hostname, boolean isNotice, Pattern pattern) {
    String[] lines = crlfPattern.split(message);
    for (String line : lines) {
      line = trailingColorAndFormat.matcher(line).replaceFirst("");
      if (line.contains("!")) {
        String plain = Colors.removeFormattingAndColors(line).trim();
        if (plain.equalsIgnoreCase("!list"))
          line = ".list";
        else if (plain.equalsIgnoreCase("!help"))
          line = ".help";
      }
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        BotModule m = moduleOfCommand(matcher.group(1));
        if (m != null) {
          if ( channel != null && settings.get(channel, "disabled") != null && !m.isAlwaysOn() ) {
            System.err.println("Ignoring command " + matcher.group(1) + " from " + sender + " (" + login + ")");
            return;
          }
          System.err.println("Processing command " + matcher.group(1) + " from " + sender + " (" + login + ")");
          try {
            String param = StringUtil.ucTrim(matcher.group(2));
            if (param.endsWith("\"")) param = param.substring(0, param.length()-1);
            m.onCommand(event = new ModuleEvent(this, channel, sender, login, hostname, message, matcher.group(1).toLowerCase(), param, isNotice));
            if (event != null && !event.processed) event = null; // If not processed, not a command afterall
            else if (!StringUtil.isEmpty(channel)) active_channel.put(channel, System.currentTimeMillis());
          } catch (StackOverflowError e) {
        	event.sendNotice(event.getLocation(), e.toString());
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          System.err.println("Found command " + matcher.group(1) + " but module not found");
        }
      }
    }
  }

  /**
   * Depart channels without command for given minutes
   * @param minute Minute to classify idle channel
   */
  public void departIdleChannel(int minute) {
    List<String> channels = new ArrayList<String>();
    for (Map.Entry<String,Long> channel : active_channel.entrySet() )
      if (System.currentTimeMillis() - channel.getValue() > minute*60*1000)
        channels.add(channel.getKey());
    for (String s : channels)
      partChannel(s);
  }

  /**
   * Update a channel's idle time to zero
   * @param channel Channel to reset
   */
  public void zeroIdleChannel(String channel) {
    if (active_channel.containsKey(channel)) active_channel.put(channel, System.currentTimeMillis());
  }

  /** Update active channel list on part */
  protected void onPart(String channel, String sender, String login, String hostname) {
    if (sender.equalsIgnoreCase(getNick())) {
      active_channel.remove(channel);
      settings.clear(channel);
    }
    super.onPart(channel, sender, login, hostname);
  }

  /** Update active channel list on being kicked */
  protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    if (recipientNick.equalsIgnoreCase(getNick())) {
      active_channel.remove(channel);
      settings.clear(channel);
    }
    super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
  }

  /** Get module that handles given command
   * @param cmd Command
   * @return Module of the command, null if none */
  public BotModule moduleOfCommand(String cmd) {
    return commandMap.get(cmd.toLowerCase());
  }

  /**
   * Check whether a user is bot admin
   * @param user User user
   * @return true if user is found and has admin password set correctly
   */
  public boolean is_admin(String user) {
    Object admin_password = data.get(IRC.KEY_ADMIN_PASSWORD);
    if (admin_password == null || admin_password.toString().length() <= 0) return true;
    else {
      String password = settings.get(user, IRC.KEY_ADMIN_PASSWORD);
      return password == null ? false : admin_password.equals(password);
    }
  }

  public boolean isInChannel(String user, String channel) {
    user = user.toLowerCase();
    User[] users = getUsers(channel);
    for (User u : users) if (u.getNick().toLowerCase().equals(user)) return true;
    return false;
  }

  /* Stop scheduled ping and reschedule a new ping */
  private void reschedulePingTimer() {
    if (pingTimer != null) pingTimer.cancel();
    if (!isConnected()) return;
    pingTimer = new Timer(true);
    pingTimer.schedule(new TimerTask(){ public void run() {
    	System.gc(); // We should be quite idle?
    	IRC.this.sendRawLine("PING " + (System.currentTimeMillis() / 1000));
		}}, 10*60*1000, 10*60*1000);
    log("Rescheduled");
  }

  /* Override to reschedule */
  protected void onServerPing(String response) {
    super.onServerPing(response);
    reschedulePingTimer();
  }



  /** Login.  Replace connect.
   * @param server hostname
   * @param port port
   * @throws IrcException
   * @throws IOException */
  public void login(String server, int port) throws IrcException, IOException {
    if (isConnected()) disconnect();
    if (!charset.equals(getEncoding())) setEncoding(charset);
    connect(server, port);
    reconnect = true;
    reschedulePingTimer();
  }

  /** Login.  Replace connect.
   * @param server hostname
   * @param port port
   * @param nick login nickname
   * @param password login password
   * @throws IrcException
   * @throws IOException */
  public void login(String server, int port, String nick, String password) throws IrcException, IOException {
    if (StringUtil.isEmpty(nick)) throw new IllegalArgumentException("Nickname must not be empty");
    if (isConnected()) disconnect();
    if (!charset.equals(getEncoding())) setEncoding(charset);
    setName(nick);
    connect(server, port, password);
    reconnect = true;
    if (!StringUtil.isEmpty(password)) identify(password);
    reschedulePingTimer();
  }

  /** Logout.  Replace disconnect. */
  public void logout() {
	logout(fullVersion);
  }

  /** Logout with reason.  Replace disconnect.
   * @param reason Reason of disconnect */
  public void logout(String reason) {
    reconnect = false;
    if (pingTimer != null) pingTimer.cancel();
    pingTimer = null;
    super.quitServer(reason);
  }

  /**
   * Set nick and change nick
   * @param nick new nick name
   */
  public void setNick(String nick) {
    setName(nick);
    if (isConnected()) changeNick(nick);
  }

  /** On disconnect, reconnect if disconnect not caused by call to logout */
  protected void onDisconnect() {
    if (reconnect) {
      System.out.println("Disconnected. Reconnect.");
      final String[] channels = getChannels(); // Get channel list before reconnect
      new Thread(new Runnable() {
        public void run() {
          int sleep = 30000; // Wait for 30 seconds
          long disconnectTime = System.currentTimeMillis();
          while (!isConnected() && reconnect) {
            if (System.currentTimeMillis() - disconnectTime > 15*60*1000) {
              System.out.println("Disconnected too long, restarting.");
              // More then 15 min; try another server
              startNewBot();
              return;
            }
            try {
              Thread.sleep(sleep);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } catch (Exception e) {
              e.printStackTrace();
            }
            System.out.println("Slept for "+sleep+" ms, reconnecting.");
            if (!isConnected() && reconnect)
              try {
                connect(getServer(), getPort(), getPassword());
              } catch (IOException e) {
                // e.g. Connection think it is opened but actually not
                // Sometimes fatal; abort and recreate bot
                startNewBot();
                return;
              } catch (IrcException e) {
                e.printStackTrace();
              } catch (Exception e) {
                e.printStackTrace();
              }
            sleep = Math.min(5 * 60 * 1000, sleep *= 1.5);
          }
          for (String c : channels) joinChannel(c);
        }

        // Start a new bot.
        private void startNewBot() {
          PnlDiceBot.instance.stopBot();
          IRC bot = PnlDiceBot.instance.startBot();
          for (String c : channels)
             bot.joinChannel(c);
        }
      }).start();
    }
    super.onDisconnect();
  }

  private static Pattern dotEscape = Pattern.compile("\\.([a-zA-Z])"); // command escape pattern

  /**
   * Deliver message to target.
   * If not connected then send to console
   * @param target channel, nickname, or USER_GUI
   * @param message Message to send, can be empty (send nothing) or in multiline
   */
  public void deliverMessage(String target, String message) {
    deliverMessage(target, message, false);
  }
  public void deliverMessage(String target, String message, boolean notice) {
    if (StringUtil.isEmpty(message)) return;
    //  Escape to avoid parsed as command by another dndbot
    message = dotEscape.matcher(message).replaceAll("."+Colors.BOLD+Colors.BOLD+"$1");
    if (pingTimer != null) pingTimer.cancel();
    pingTimer = null;
    for (String l : StringUtil.splitLines(message)) // Split multiline message
      if (Colors.removeFormattingAndColors(l).trim().length() > 0)
        if (target.equals(IRC.USER_GUI)) {
          if (PnlDiceBot.instance != null) PnlDiceBot.instance.addConsole("< "+l);
        } else if (isConnected()) {
            String theme = settings.get(target, "colour");
            if (theme == null) theme = settings.get(target, "color");
            if (theme != null && (theme.equals("no") || theme.equals("off") || theme.equals("none")) ) {
            	l = Colors.removeFormattingAndColors(l);
            }
            if (target.startsWith("#")) {
            if (!notice)
              sendMessage(target, l);
            else
              sendNotice(target, l);
            ModuleEvent m = event;
            // Temporary disable event to simulate normal message so that it will be logged normally
            event = null;
            onMessage(target, getNick(), getLogin(), null, l);
            event = m;
          } else {
            if (!notice)
              sendMessage(target, l);
            else
              sendNotice(target, l);
          }
        } else
          System.out.println(getNick()+": "+Colors.removeFormattingAndColors(l));
    reschedulePingTimer();
  }

  public void deliverAction(String target, String message) {
    for (String action : StringUtil.splitLines(message))
      if (Colors.removeFormattingAndColors(action).trim().length() > 0)
        if (target.equals(IRC.USER_GUI)) {
          if (PnlDiceBot.instance != null) PnlDiceBot.instance.addConsole("* "+getNick()+" "+action);
        } else if (isConnected()) {
          sendAction(target, action);
          if (target.startsWith("#")) {
            ModuleEvent m = event;
            event = null;
            onAction(getNick(), getLogin(), null, target, action);
            event = m;
          }
        } else
          System.out.println(getNick()+" "+Colors.removeFormattingAndColors(action));
  }


  private static Pattern whitespaceColour = Pattern.compile("\u0003(?:\\d{2})("+StringUtil.ucSpace.pattern()+"+\u0003)");
  private static Pattern dupColour = Pattern.compile("(\u0003(\\d{2})[^\u0003]*)\u0003(\\2)");
  public static String optimiseColours(String in) {
    String buf = in;
    do {
      in = buf;
      Matcher m = whitespaceColour.matcher(buf);
      buf = m.replaceAll("$1");
      m = dupColour.matcher(buf);
      buf = m.replaceAll("$1");
    } while (!buf.equals(in));
    return buf;
  }


  /**
   * Dual-key Map
   */
  public class Settings {
    private final Map<String, Map<String, String>> settings = new HashMap<String, Map<String, String>>();

    public String get(String user, String key) {
      return get(user, key, false, null);
    }

    /**
     * Get user / channel setting
     * @param user User's nick
     * @param key Setting key
     * @param checkDM If true, search DM when not found
     * @param checkChannel If true, search channel when not found
     * @return String data or null
     */
    public String get(String user, String key, boolean checkDM, String checkChannel) {
      if (key != null) key = key.toLowerCase();
      if (user != null) user = user.toLowerCase();
      Map<String, String> conf = settings.get(user);
      String result = (conf == null) ? null :conf.get(key);
      if (result == null && checkDM) {
        result = get(user, "DM");
        if (result != null) {
          result = get(result, key);
        }
      }
      if (result == null && checkChannel != null) {
        result = get(checkChannel, key);
      }
      return result;
    }

    public void put(String user, String key, String value) {
      if (key != null) key = key.toLowerCase();
      if (user != null) user = user.toLowerCase();
      Map<String, String> conf = settings.get(user);
      if (conf == null && value != null) {
        conf = new HashMap<String, String>();
        settings.put(user, conf);
      }
      if (value != null)
        conf.put(key, value);
      else
        if (conf != null) conf.remove(key);
    }

    public void clear(String user) {
      settings.remove(user);
    }
  }

}
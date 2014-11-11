package dndbot.module;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.jibble.pircbot.Colors;
import org.sheepy.util.StringUtil;

import dndbot.irc.IRC;

/**
 * Core module.  Handles system commands including help, set, reset, reconnect, shutdown, etc.
 *
 * Created by Ho Yiu YEUNG on Mar 9, 2007
 */
public class Core extends AbstractBotModule {
  private static final Runtime runtime = Runtime.getRuntime();

  public Core(IRC irc) {
    super(irc);
  }

  public boolean isAlwaysOn() { return true; }

  public String getCommandPattern() {
    return "help|set|list|dndbot|on|off";
  }

  public String[] getCommand() {
    return new String[]{"help", "set", "list", "dndbot", "on", "off"};
  }


  public String getDirectCommandPattern() {
    return getCommandPattern()+"|me|nick|shutdown|re(?:set|connect)|gc|list";
  }

  public String[] getDirectCommand() {
    return new String[]{"help", "set", "me", "nick", "shutdown", "reset", "gc", "reconnect", "list", "dndbot", "on", "off"};
  }

  public void onCommand(final ModuleEvent evt) {
    final String[] param = evt.parameter.split("\\s+", 2);
    final boolean is_admin = irc.is_admin(evt.sender);

      // help
    if (evt.command.equals("help")) {
      if (evt.parameter.length() <= 0) {
        // no command specified; list commands
        evt.sendMessage(evt.sender, IRC.version);
        evt.sendMessage(evt.sender, res.getString("help_help"));
        for (BotModule m : irc.mods) evt.sendMessage(evt.sender, m.getHelp("summary"));
      } else {
        // Check and get help for specific command
        String cmd = Colors.removeFormattingAndColors(evt.parameter.split("\\s+", 2)[0]);
        if (cmd.startsWith(".")) cmd = cmd.substring(1);
        BotModule m = irc.moduleOfCommand(cmd);
        String result = null;
        if (m == null)
          result = res.getString("errNoCmd");
        else
          result = m.getHelp(evt.parameter);
        if (!StringUtil.isEmpty(result)) {
          evt.sendMessage(evt.sender, result);
          if (result.startsWith(Colors.RED)) return;
        } else {
          evt.sendMessage(evt.sender, MessageFormat.format(res.getString("errNoHelp"), cmd));
          return;
        }
      }
//      if (evt.channel != null) // Remind them that help can be acquired from private message
//        evt.sendMessage(evt.sender, res.getString("private_help"));

      // set
    } else if (evt.command.equals("set")) {
      if (param.length < 2) {
        // Clear parameter
        if (param[0].length() > 0) {
          irc.settings.put(evt.getLocation(), param[0], null);
          if (evt.channel != null) irc.settings.put(evt.sender, param[0], null);
          evt.sendNotice(evt.sender, MessageFormat.format(res.getString("setNone"), param[0]));
        }
        return;
      } else {
        String key = param[0];
        String value = param[1];
        if (key.length() == 0 || value.length() == 0) return;
        if (key.equalsIgnoreCase("Dice") && value.toLowerCase().startsWith("d"))
          value = value.substring(1);
        irc.settings.put(evt.getLocation(), key, value);
        if (evt.channel != null) irc.settings.put(evt.sender, key, value);
        evt.sendNotice(evt.sender, MessageFormat.format(res.getString("setDone"), key, value));
      }

      // list
    } else if (evt.command.equals("list")) {
      irc.sendNotice(evt.sender, res.getString("list"));

      // on
    } else if (evt.command.equals("on")) {
      String channel = evt.channel;
      if (channel == null) {
        channel = Colors.removeFormattingAndColors(evt.parameter).trim();
        if (!StringUtil.isEmpty(channel) && !channel.startsWith("#")) channel = "#" + channel;
      }
      if (!StringUtil.isEmpty(channel)) {
        if (irc.settings.get(channel, "disabled") != null)
          evt.sendAction(channel, res.getString("on"));
        irc.settings.put(channel, "disabled", null);
      }

      // off
    } else if (evt.command.equals("off")) {
      String channel = evt.channel;
      if (channel == null) {
        channel = Colors.removeFormattingAndColors(evt.parameter).trim();
        if (!StringUtil.isEmpty(channel) && !channel.startsWith("#")) channel = "#" + channel;
      }
      if ( StringUtil.isEmpty(evt.channel) ||
           ( !irc.is_admin(evt.sender) && (!irc.isInChannel(irc.getNick(), channel) || !irc.isInChannel(evt.sender, channel)) )
          ) {
    	evt.sendNotice(evt.sender, res.getString("errNotInChannel"));
     } else {
        evt.sendAction(channel, res.getString("off"));
        evt.getBot().settings.put(evt.channel, "disabled", "true");
      }

      // dndbot
    } else if (evt.command.equals("dndbot")) {
      if (evt.channel != null) {
        String p = evt.parameter;
        if (!p.startsWith(".")) p = "."+p;
        irc.processMessage(p, evt.channel, evt.sender, evt.login, evt.hostname);
      } else
        irc.processPrivateMessage(evt.parameter, evt.sender, evt.login, evt.hostname);

      // nick
    } else if (evt.command.equals("nick")) {
      if (!is_admin) { evt.sendMessage(evt.getLocation(), res.getString("errNotAdmin")); return; }
      irc.setNick(evt.parameter);

      // me
    } else if (evt.command.equals("me")) {
      String destination = evt.resultChannel(evt.sender);
      if (is_admin || destination == null || irc.is_channel_admin(evt.sender, destination)) {
        if (destination == null) destination = evt.sender;
        evt.sendAction(destination, evt.parameter);
      } else {
        evt.sendNotice(evt.getLocation(), res.getString("errNotAdmin"));
        return;
      }

      // gc
    } else if (evt.command.equals("gc")) {
      if (!is_admin) { evt.sendMessage(evt.getLocation(), res.getString("errNotAdmin")); return; }
      long memory = runtime.totalMemory() - runtime.freeMemory();
      evt.sendNotice(evt.sender, res.getString("gc"));
      gc();
      long now = runtime.totalMemory() - runtime.freeMemory();
      evt.sendNotice(evt.sender, MessageFormat.format(res.getString("gcDone"), (memory - now)));

      // reset
    } else if (evt.command.equals("reset")) {
      if (!is_admin) { evt.sendMessage(evt.getLocation(), res.getString("errNotAdmin")); return; }
      evt.sendNotice(evt.sender, res.getString("resetting"));
      try {
        ResourceBundle.clearCache();
      } catch (NoSuchMethodError e) {
        evt.sendNotice(evt.sender, res.getString("errOldJre"));
      }
      irc.reset();  // IRC creates new module on reset, module reset unnecessary
      gc();
      evt.sendNotice(evt.sender, res.getString("resetted"));

      // reconnect
    } else if (evt.command.equals("reconnect")) {
      if (!is_admin) { evt.sendNotice(evt.getLocation(), res.getString("errNotAdmin")); return; }
      final String[] channels = irc.getChannels();
      irc.logout(MessageFormat.format(res.getString("reconnecting"), evt.sender));
      gc();
      new Thread(new Runnable() { public void run() {
        while (irc.isConnected()) try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          return;
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        try {
          irc.setAutoNickChange(true);
          irc.reconnect();
          for (String c : channels) irc.joinChannel(c);
          evt.sendNotice(evt.sender, res.getString("reconnected"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }}).start();

      // shutdown
    } else if (evt.command.equals("shutdown")) {
      if (!is_admin) { evt.sendNotice(evt.getLocation(), res.getString("errNotAdmin")); return; }
      irc.sendRawLine("PRIVMSG " + evt.sender + " :" + res.getString("shutdown"));
      if (evt.parameter.length() > 0)
        irc.logout(evt.parameter);
      else
        irc.logout(IRC.version);
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.exit(0);
    }
    evt.processed = true;
  }

  /**
   * Garbage collect a few times
   */
  private void gc() {
    for (int i = 0; i < 5; i++) {
      System.gc();
      runtime.runFinalization();
      Thread.yield();
    }
  }

}

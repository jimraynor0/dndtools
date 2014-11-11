package dndbot.module;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.DccFileTransfer;
import org.sheepy.util.StringUtil;

import dndbot.irc.IRC;
import dndbot.module.log.LogRecord;

/**
 * Log recording and transforming module
 *
 * Created by Ho Yiu Yeung on Mar 16, 2007
 */
public class Log extends AbstractBotModule {
  public static final String DATA_LOGS = "LOG_LOGS";

  /** Temporary log file prefix, generated before dcc file transfer */
  private static final String TempFilePrefix = "log-";
  private static final Charset utf8 = Charset.forName("UTF-8");

  /** A map of channel > log;  The real log resides in irc.data to survive reset. */
  private final Map<String, LogRecord> logs;

  @SuppressWarnings("unchecked")
  public Log(IRC irc) {
    super(irc);
    Object l = irc.data.get(DATA_LOGS);
    if (l == null) {
      l = new HashMap<String, LogRecord>(5, 1.0f);
      irc.data.put(DATA_LOGS, l);
    }
    logs = (Map<String, LogRecord>) l;
    irc.addListener(this);
  }


  public String getCommandPattern() {
    return "log";
  }

  public String[] getCommand() {
    return new String[] { "log" };
  }

  public String getDirectCommandPattern() {
    return null;
  }

  public String[] getDirectCommand() {
    return new String[] {};
  }

  public void reset() {
    irc.removeListener(this);
    super.reset();
  }

  public void onCommand(ModuleEvent evt) {
    if (evt.command.equals("log")) {
      String command = evt.parameter.split("\\s+", 2)[0];
      if (StringUtil.isEmpty(command)) return;
      evt.processed = true;
      LogRecord log = null;
      synchronized (logs) {
        log = logs.get(evt.channel);
      }

      if (command.equals("start") || command.equals("begin")) {
        if (log != null)
          evt.sendMessage(evt.channel, (log.enabled) ? res.getString("errAlreadyLogging") :  res.getString("errAlreadyHasLog"));
        else {
          log = createLog(evt.channel);
          log.enabled = true;
          evt.sendMessage(evt.channel, res.getString("logStart"));
        }

      } else if (command.equals("stop") || command.equals("end")
          || command.equals("pause")) {
        if (log == null)
          evt.sendMessage(evt.channel, res.getString("errNoLog"));
        else {
          log.enabled = false;
          evt.sendMessage(evt.channel, res.getString("logStop"));
        }

      } else if (command.equals("resume")) {
        if (log == null)
          evt.sendMessage(evt.channel, res.getString("errNoLog"));
        else {
          log.enabled = true;
          evt.sendMessage(evt.channel, res.getString("logResume"));
        }

      } else if (command.equals("restart")) {
        if (log == null)
          evt.sendMessage(evt.channel, res.getString("errNoLog"));
        else {
          log.reset(new Date());
          log.enabled = true;
          evt.sendMessage(evt.channel, res.getString("logReset"));
        }

      } else if (command.equals("clear")) {
        if (log == null)
          evt.sendMessage(evt.channel, res.getString("errNoLog"));
        else {
          synchronized (logs) {
            logs.remove(evt.channel);
          }
          evt.sendMessage(evt.channel, res.getString("logClear"));
        }

      } else if (command.equals("get")) {
        LogRecord l = logs.get(evt.channel);
        if (l == null) { // Check that we have log
          evt.sendMessage(evt.channel, res.getString("errNoLog"));
          return;
        }
        // Check and get log type
        String[] temp = evt.parameter.split("\\s+", 2);
        String type = "xml";
        if (temp.length == 2 && temp[1].trim().length() > 0) type = temp[1].trim().toLowerCase();
        String prefix = TempFilePrefix+evt.channel+"-"+Calendar.getInstance().getTimeInMillis(); //LogRecord.dateTimeFormat.format(new Date());
        File f;
        try {
          // Create temporary log file to be sent
          f = File.createTempFile(prefix, "."+type);
          f.deleteOnExit();
          saveLog(l.get(), type, f);
          // Done, start the transfer
          evt.sendMessage(evt.sender, MessageFormat.format(res.getString("logGet"), type.toUpperCase(), evt.channel));
          irc.dccSendFile(f, evt.sender, 120000);
        } catch (UnsupportedFlavorException e) {
          evt.sendMessage(evt.sender, res.getString("errUnknownType"));
//          return;
        } catch (TransformerException e) {
          e.printStackTrace();
          evt.sendMessage(evt.sender, e.toString());
//          return;
        } catch (IOException e) {
          evt.sendMessage(evt.sender, res.getString("errSaveLogFail"));
          e.printStackTrace();
//          return;
        } catch (Exception e) {
          e.printStackTrace();
//          return;
        }
      } // if (command.equals("get"))
    } // if (evt.command.equals("log"))
  }

  /**
   * Save log into given file in given type
   *
   * @param l Log to save
   * @param type File type, maybe xml, html, or bbc
   * @param f File to save to
   * @throws UnsupportedFlavorException If transformation of given type is not found
   * @throws TransformerException If there's some problem in transformation
   * @throws IOException If there's problem in reading / saving
   */
  public static void saveLog(String l, String type, File f) throws UnsupportedFlavorException, TransformerException, IOException {
    Transformer transform = null;
    type = type.toLowerCase();
    System.err.println("Saveing log in "+type+"to "+f.getAbsolutePath());
    if (!type.equals("xml")) {
      InputStream xsl = new BufferedInputStream(new FileInputStream(new File("transform/log-"+type+".xsl")));
      if (xsl == null) throw new UnsupportedFlavorException(null);
      transform = TransformerFactory.newInstance().newTransformer(new StreamSource(xsl));
    }
    // Save and send log
    if (transform == null) {
      PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), utf8));
      out.write(l);
      out.close();
    } else {
      StreamSource in = new StreamSource(new ByteArrayInputStream(l.getBytes(utf8.toString())));
      StreamResult out = new StreamResult(new BufferedOutputStream(new FileOutputStream(f)));
//          StreamResult out = new StreamResult(f);
      transform.transform(in, out);
      out.getOutputStream().close();
      in.getInputStream().close();
    }
  }

  /**
   * Make sure that a log for given channel exists
   *
   * @param channel Channel to check
   * @return Log for this channel
   */
  private LogRecord createLog(String channel) {
    synchronized (logs) {
      if (!logs.containsKey(channel)) {
        LogRecord log = new LogRecord(new Date(), channel);
        logs.put(channel, log);
        return log;
      }
      return logs.get(channel);
    }
  }

  /**
   * Delete file on transfer finished
   */
  public boolean onFileTransferFinished(DccFileTransfer transfer, Exception e) {
    if (e == null) { // && transfer.getFile().getName().startsWith("log-")) {
      transfer.getFile().delete();
    } else {
      e.printStackTrace();
      transfer.getFile().delete(); // delete anyway; let user re-request instead
//      irc.dccSendFile(transfer.getFile(), transfer.getNick(), 120000);
    }
    return super.onFileTransferFinished(transfer, e);
  }

  /**
   * If log exists for a channel and message is not error, forward the message to log
   */
  public boolean onMessage(String channel, String sender, String login, String hostname, String message) {
    if (irc.event == null || !Colors.removeFormattingAndColors(irc.event.message).startsWith(".")) synchronized (logs) {
      if (logs.containsKey(channel) && !(irc.getLogin().equalsIgnoreCase(login) && message.startsWith(Colors.RED))) {
        if (logs.get(channel).logMessage(new Date(), sender, login, message)) irc.zeroIdleChannel(channel);
      }
    }
    return super.onMessage(channel, sender, login, hostname, message);
  }

  /**
   * If action to a channel and log exists, forward the action to log
   */
  public boolean onAction(String sender, String login, String hostname, String target, String action) {
    if (target.startsWith("#")) synchronized (logs) { if (logs.containsKey(target)) {
      if (logs.get(target).logAction(new Date(), sender, login, action)) irc.zeroIdleChannel(target);
    }}
    return super.onAction(sender, login, hostname, target, action);
  }

  /**
   * Log any topic change, since it may be used to keep track of game status
   */
  public boolean onTopic(String channel, String topic, String setBy, long date, boolean changed) {
    synchronized (logs) {
      if (logs.containsKey(channel)) {
        if (logs.get(channel).logTopic(new Date(), setBy, topic)) irc.zeroIdleChannel(channel);
      }
    }
    return super.onTopic(channel, topic, setBy, date, changed);
  }




  /** Control code pattern, used to seek for code to be converted to xml */
  private static final Pattern codePattern = Pattern
      .compile("(?:\\x02|\\x0F|\\x11|\\x12|\\x16|\\x1D|\\x1F|" + // Bold, TrueColor, Normal, Fixed, Reverse, ?, Italic, Underline
          "\\x03[\\dA-F]{0,2}(?:,[\\dA-F]{0,2})?|" + // mIRC colour
          "\\x04[\\dA-F]{6}(?:,(?:[\\dA-F]{6})?)?|" +
          "\\x04)" // RGB colour
      );

  private static final Pattern emptyCodePattern = Pattern.compile("(" +
      "<color code='[^']*'></color>" +
      "|<background code='[^']*'></background>" +
      "|<(\\w+)></\\2>" +
      ")");
  /*
  private static final Pattern unusedColourPattern = Pattern.compile(
      "<color code='[^']*'>(<color code='[^']*'>(?:[^/]|</[^c][^>]+>)+" +
      "</color>)</color>");
  */

  private static final String defaultForeground = "default";

  private static final String defaultBackground = "default";

  /**
   * Translate multiline mIRC coded string into XML
   *
   * @param in IRC string
   * @return input in XML format
   */
  public static String translateAll(String in) {
    StringBuilder out = new StringBuilder((int) (in.length()*1.4));
    for (String line : StringUtil.splitLines(in)) {
      translate(line, out);
      out.append("\n");
    }
    return out.toString();
  }

  /**
   * Translate an mIRC coded string into XML
   *
   * @param message String to translate
   * @param out Buffer to append to
   */
  public static void translate(String message, StringBuilder out) {
    message = StringEscapeUtils.escapeXml(StringEscapeUtils.unescapeHtml(IRC.optimiseColours(message)));
    Matcher matcher = codePattern.matcher(message);
//    final Deque<String> tag = new ArrayDeque<String>(40); // Use list for compatibility
    final List<String> tag = new ArrayList<String>(40);
    String foreground = defaultForeground;
    String background = defaultBackground;
    int left = 0;

    StringBuilder buf = new StringBuilder();

    boolean bold = false;
    boolean fix = false;
    boolean reverse = false;
    boolean i = false;
    boolean u = false;

    while (matcher.find()) {
      if (matcher.start() > left) {
        String text = message.substring(left, matcher.start());
        buf.append(text);
      }
      left = matcher.end();

      // Handle command
      char command = matcher.group(0).charAt(0);
      if (command == '\u000F') {

        bold = fix = reverse = i = u = false;

        // Normal, close all tag
        while (tag.size() > 0)
          buf.append("</").append(tag.remove(tag.size()-1)).append(">");

      } else if (command == '\u0003' || command == '\u0004') {
        // Color code
        String code = matcher.group(0).substring(1);
        if (code.contains(",")) {
          // [code]?,? : set foreground and background
          int pos = code.indexOf(',');
          foreground = code.substring(0, pos);
          background = code.substring(pos + 1);
          if (foreground.length() == 0) foreground = defaultForeground;
          if (background.length() == 0) background = defaultBackground;
        } else if (code.length() > 0) {
          // [code]00 : set foreground
          foreground = code;
          background = null;
        } else {
          // [code] : reset foreground and background
          foreground = defaultForeground;
          background = defaultBackground;
        }
        if (command == '\u0004') { // Full rgb colour
          if (foreground != null) foreground = "#" + foreground;
          if (background != null) background = "#" + background;
        } else { // mIRC code
          if (foreground != null && foreground.length() < 2)
            foreground = "0" + foreground;
          if (background != null && background.length() < 2)
            background = "0" + background;
        }
        if (foreground != null) {
          if (!tag.isEmpty() && tag.get(tag.size()-1).equals("color")) {
            buf.append("</color>");
          } else {
            tag.add("color");
          }
          buf.append("<color code='").append(foreground).append("'>");
        }
        if (background != null) {
          if (!tag.isEmpty() && tag.get(tag.size()-1).equals("background")) {
            buf.append("</background>");
          } else {
            tag.add("background");
          }
          buf.append("<background code='").append(background).append("'>");
        }

      } else {
        // Usual style
        String t = null;
        switch (command) {
          case '\u0002': if (!bold) t = "b"; bold = !bold; break;
          case '\u0011': if(!fix) t = "fix"; fix = !fix; break;
          case '\u0012': if(!reverse) t = "reverse"; reverse = !reverse; break;
          case '\u0016': t = "custom"; break;
          case '\u001D': if (!i) t = "i"; i = !i; break;
          case '\u001F': if (!u) t = "u"; u = !u;break;
        }
        if (t != null) {
          tag.add(t);
          buf.append("<").append(t).append(">");
        } else {
          buf.append("</").append(tag.remove(tag.size()-1)).append(">");
        }
      }
    } // while (matcher.find())
    if (left < message.length()) buf.append(message.substring(left));
    while (tag.size() > 0)
      buf.append("</").append(tag.remove(tag.size()-1)).append(">");

    // Remove empty tags until there are no empty tags
    String result = buf.toString();
    Matcher m = emptyCodePattern.matcher(result);
    while (m.find()) {
      result = m.replaceAll("");
      m.reset(result);
    }
    /*
    m = unusedColourPattern.matcher(filteredResult);
    while (m.find()) {
      filteredResult = m.replaceFirst(m.group(1));
      m.reset(filteredResult);
    }
    */
    out.append(result);
  }

}

package dndbot.module.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import dndbot.module.Log;

/**
 * An individual log
 * 
 * Created by Ho Yiu YEUNG on 2007 March 19
 */
public class LogRecord {
//  private Date start;
  private final String channel;
  private final StringBuilder log = new StringBuilder(8192);
  public boolean enabled;
  public static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); 
  public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

  public LogRecord(Date start, String channel) {
//    this.start = start;
    this.channel = channel;
    reset(start);
  }
  
  public synchronized void reset(Date start) {
//    this.start = start;
    log.setLength(0);
    log.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><channel>").append(StringEscapeUtils.escapeXml(channel)).append("</channel><start>").append(dateTimeFormat.format(start)).append("</start><log>\n");
  }

  public synchronized boolean logMessage(Date time, String sender, String login, String message) {
    if (!enabled) return false;
    log.append("<message time='"+timeFormat.format(time)+"' sender='"+ StringEscapeUtils.escapeXml(sender) +"' login='"+ StringEscapeUtils.escapeXml(login) +"'>");
    Log.translate(message, log);
    log.append("</message>");
    return true;
  }

  public synchronized boolean logAction(Date time, String sender, String login, String action) {
    if (!enabled) return false;
    log.append("<action time='"+timeFormat.format(time)+"' sender='"+ StringEscapeUtils.escapeXml(sender) +"' login='"+ StringEscapeUtils.escapeXml(login) +"'>");
    Log.translate(StringEscapeUtils.escapeXml(action), log);
    log.append("</action>");
    return true;
  }

  public synchronized boolean logTopic(Date date, String setBy, String topic) {
    if (!enabled) return false;
    log.append("<topic time='"+timeFormat.format(date)+"' setBy='"+ StringEscapeUtils.escapeXml(setBy) +"'>");
    Log.translate(topic, log);
    log.append("</topic>");
    return true;
  }

  /** Return complete log in xml form 
   * @return log string */
  public synchronized String get() {
    return log.toString()+"</log></xml>";
  }

  public String toString() {
    return (enabled)? channel : channel + " (stopped)"; 
  }

}

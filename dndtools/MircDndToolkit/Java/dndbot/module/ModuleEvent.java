package dndbot.module;

import dndbot.irc.IRC;

/**
 * A module command event; carries only the event and not filteredResult but act as a bridge of communication.
 *
 * Created by Ho Yiu YEUNG on Mar 9, 2007
 */
public class ModuleEvent {
  final IRC bot;
  public final String channel;
  public final boolean isNotice;
  public final String sender;
  public final String login;
  public final String hostname;
  public final String message;
  public final String command;
  public final String parameter;
  public boolean processed = false;

  public ModuleEvent(IRC bot, String channel, String sender, String login, String hostname, String message, String command, String parameter, boolean isNotice) {
    this.bot = bot;
    this.channel = channel;
    this.isNotice = isNotice;
    this.sender = sender;
    this.login = login;
    this.hostname = hostname;
    this.message = message;
    this.command = command;
    this.parameter = parameter;
  }

  /**
   * Return channel name if the command is sent in a channel, otherwise sender nick.
   * @return channel name or sender nick.
   */
  public String getLocation() {
    return (channel != null) ? channel : sender;
  }

  /**
   * Return channel name if the command is sent in a channel, otherwise sender login.
   * @return channel name or sender login.
   */
  public String getWhere() {
    return (channel != null) ? channel : login;
  }

  /**
   * Send a message to target
   * @param target Channel or nick
   * @param message Message to send
   */
  public void sendMessage(String target, String message) {
    if (!isNotice) bot.deliverMessage(target, message, false);
  }

  /**
   * Send a notice to target
   * @param target Channel or nick
   * @param message Message to send
   */
  public void sendNotice(String target, String message) {
    if (!isNotice) bot.deliverMessage(target, message, true);
  }

  /**
   * Send an action to target
   * @param target Channel or nick
   * @param message Action to send
   */
  public void sendAction(String target, String message) {
    if (!isNotice) bot.deliverAction(target, message);
  }

  /**
   * Get filteredResult channel of given user
   * If none then returns event channel (which again may be null)
   * @param sender Login of user
   * @return null or user's output channel
   */
  public String resultChannel(String sender) {
    String result = bot.settings.get(sender, "output");
    if (result != null && !result.startsWith("#")) result = "#"+result;
    return (result == null) ? channel : result;
  }

  public IRC getBot() {
    return bot;
  }
}

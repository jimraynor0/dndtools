package org.sheepy.pirc;

import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

/**
 * A PircBot that send out PircEvents
 *
 * @author Ho Yiu YEUNG
 * @since 2007 Mar 17
 */
public abstract class PircSubject extends PircBot {
  protected List<PircListener> listeners = null;

  protected void addListener(PircListener listener) {
    if (listener == null) return;
    if (listeners == null) listeners = new ArrayList<PircListener>();
    listeners.add(listener);
  }

  protected void removeListener(PircListener listener) {
    if (listener == null || listeners == null) return;
    listeners.remove(listener);
    if (listeners.isEmpty()) listeners = null;
  }

  /**
   * Check whether a user is channel op
   * @param nick User nickname
   * @param channel Channel
   * @return true if user is found and is op, otherwise false
   */
  public boolean is_channel_admin(String nick, String channel) {
    User[] users = getUsers(channel);
    for (User u : users) {
      if (u.getNick().equalsIgnoreCase(nick)) {
        return u.isOp();
      }
    }
    return false;
  }

  /**
   * This method is called once the PircBot has successfully connected to
   * the IRC server.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @since PircBot 0.9.6
   */
  protected void onConnect() {
    super.onConnect();
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onConnect()) break;
  }

  /**
   * This method carries out the actions to be performed when the PircBot
   * gets disconnected.  This may happen if the PircBot quits from the
   * server, or if the connection is unexpectedly lost.
   * <p/>
   * Disconnection from the IRC server is detected immediately if either
   * we or the server close the connection normally. If the connection to
   * the server is lost, but neither we nor the server have explicitly closed
   * the connection, then it may take a few minutes to detect (this is
   * commonly referred to as a "ping timeout").
   * <p/>
   * If you wish to get your IRC bot to automatically rejoin a server after
   * the connection has been lost, then this is probably the ideal method to
   * override to implement such functionality.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   */
  protected void onDisconnect() {
    super.onDisconnect();
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onDisconnect()) break;
  }

  /**
   * This method is called when we receive a numeric response from the
   * IRC server.
   * <p/>
   * Numerics in the range from 001 to 099 are used for client-server
   * connections only and should never travel between servers.  Replies
   * generated in response to commands are found in the range from 200
   * to 399.  Error replies are found in the range from 400 to 599.
   * <p/>
   * For example, we can use this method to discover the topic of a
   * channel when we join it.  If we join the channel #test which
   * has a topic of &quot;I am King of Test&quot; then the response
   * will be &quot;<code>PircBot #test :I Am King of Test</code>&quot;
   * with a code of 332 to signify that this is a topic.
   * (This is just an example - note that overriding the
   * <code>onTopic</code> method is an easier way of finding the
   * topic for a channel). Check the IRC RFC for the full list of other
   * command response codes.
   * <p/>
   * PircBot implements the interface ReplyConstants, which contains
   * contstants that you may find useful here.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param code     The three-digit numerical code for the response.
   * @param response The full response from the IRC server.
   * @see org.jibble.pircbot.ReplyConstants
   */
  protected void onServerResponse(int code, String response) {
    super.onServerResponse(code, response);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onServerResponse(code, response)) break;
  }

  /**
   * This method is called when we receive a user list from the server
   * after joining a channel.
   * <p/>
   * Shortly after joining a channel, the IRC server sends a list of all
   * users in that channel. The PircBot collects this information and
   * calls this method as soon as it has the full list.
   * <p/>
   * To obtain the nick of each user in the channel, call the getNick()
   * method on each User object in the array.
   * <p/>
   * At a later time, you may call the getUsers method to obtain an
   * up to date list of the users in the channel.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel The name of the channel.
   * @param users   An array of User objects belonging to this channel.
   * @see org.jibble.pircbot.User
   * @since PircBot 1.0.0
   */
  protected void onUserList(String channel, User[] users) {
    super.onUserList(channel, users);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onUserList(channel, users)) break;
  }

  /**
   * This method is called whenever a message is sent to a channel.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel  The channel to which the message was sent.
   * @param sender   The nick of the person who sent the message.
   * @param login    The login of the person who sent the message.
   * @param hostname The hostname of the person who sent the message.
   * @param message  The actual message sent to the channel.
   */
  protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    super.onMessage(channel, sender, login, hostname, message);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onMessage(channel, sender, login, hostname, message)) break;
  }

  /**
   * This method is called whenever a private message is sent to the PircBot.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param sender   The nick of the person who sent the private message.
   * @param login    The login of the person who sent the private message.
   * @param hostname The hostname of the person who sent the private message.
   * @param message  The actual message.
   */
  protected void onPrivateMessage(String sender, String login, String hostname, String message) {
    super.onPrivateMessage(sender, login, hostname, message);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onPrivateMessage(sender, login, hostname, message)) break;
  }

  /**
   * This method is called whenever an ACTION is sent from a user.  E.g.
   * such events generated by typing "/me goes shopping" in most IRC clients.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param sender   The nick of the user that sent the action.
   * @param login    The login of the user that sent the action.
   * @param hostname The hostname of the user that sent the action.
   * @param target   The target of the action, be it a channel or our nick.
   * @param action   The action carried out by the user.
   */
  protected void onAction(String sender, String login, String hostname, String target, String action) {
    super.onAction(sender, login, hostname, target, action);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onAction(sender, login, hostname, target, action)) break;
  }

  /**
   * This method is called whenever we receive a notice.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param sourceNick     The nick of the user that sent the notice.
   * @param sourceLogin    The login of the user that sent the notice.
   * @param sourceHostname The hostname of the user that sent the notice.
   * @param target         The target of the notice, be it our nick or a channel name.
   * @param notice         The notice message.
   */
  protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
    super.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice)) break;
  }

  /**
   * This method is called whenever someone (possibly us) joins a channel
   * which we are on.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel  The channel which somebody joined.
   * @param sender   The nick of the user who joined the channel.
   * @param login    The login of the user who joined the channel.
   * @param hostname The hostname of the user who joined the channel.
   */
  protected void onJoin(String channel, String sender, String login, String hostname) {
    super.onJoin(channel, sender, login, hostname);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onJoin(channel, sender, login, hostname)) break;
  }

  /**
   * This method is called whenever someone (possibly us) parts a channel
   * which we are on.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel  The channel which somebody parted from.
   * @param sender   The nick of the user who parted from the channel.
   * @param login    The login of the user who parted from the channel.
   * @param hostname The hostname of the user who parted from the channel.
   */
  protected void onPart(String channel, String sender, String login, String hostname) {
    super.onPart(channel, sender, login, hostname);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onPart(channel, sender, login, hostname)) break;
  }

  /**
   * This method is called whenever someone (possibly us) changes nick on any
   * of the channels that we are on.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param oldNick  The old nick.
   * @param login    The login of the user.
   * @param hostname The hostname of the user.
   * @param newNick  The new nick.
   */
  protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
    super.onNickChange(oldNick, login, hostname, newNick);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onNickChange(oldNick, login, hostname, newNick)) break;
  }

  /**
   * This method is called whenever someone (possibly us) is kicked from
   * any of the channels that we are in.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel        The channel from which the recipient was kicked.
   * @param kickerNick     The nick of the user who performed the kick.
   * @param kickerLogin    The login of the user who performed the kick.
   * @param kickerHostname The hostname of the user who performed the kick.
   * @param recipientNick  The unfortunate recipient of the kick.
   * @param reason         The reason given by the user who performed the kick.
   */
  protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason)) break;
  }

  /**
   * This method is called whenever someone (possibly us) quits from the
   * server.  We will only observe this if the user was in one of the
   * channels to which we are connected.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param sourceNick     The nick of the user that quit from the server.
   * @param sourceLogin    The login of the user that quit from the server.
   * @param sourceHostname The hostname of the user that quit from the server.
   * @param reason         The reason given for quitting the server.
   */
  protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onQuit(sourceNick, sourceLogin, sourceHostname, reason)) break;
  }


  /**
   * This method is called whenever a user sets the topic, or when
   * PircBot joins a new channel and discovers its topic.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel The channel that the topic belongs to.
   * @param topic   The topic for the channel.
   * @param setBy   The nick of the user that set the topic.
   * @param date    When the topic was set (milliseconds since the epoch).
   * @param changed True if the topic has just been changed, false if
   *                the topic was already there.
   */
  protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
    super.onTopic(channel, topic, setBy, date, changed);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onTopic(channel, topic, setBy, date, changed)) break;
  }

  /**
   * After calling the listChannels() method in PircBot, the server
   * will start to send us information about each channel on the
   * server.  You may override this method in order to receive the
   * information about each channel as soon as it is received.
   * <p/>
   * Note that certain channels, such as those marked as hidden,
   * may not appear in channel listings.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel   The name of the channel.
   * @param userCount The number of users visible in this channel.
   * @param topic     The topic for this channel.
   * @see #listChannels() listChannels
   */
  protected void onChannelInfo(String channel, int userCount, String topic) {
    super.onChannelInfo(channel, userCount, topic);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onChannelInfo(channel, userCount, topic)) break;
  }

  /**
   * Called when the mode of a channel is set.
   * <p/>
   * You may find it more convenient to decode the meaning of the mode
   * string by overriding the onOp, onDeOp, onVoice, onDeVoice,
   * onChannelKey, onDeChannelKey, onChannelLimit, onDeChannelLimit,
   * onChannelBan or onDeChannelBan methods as appropriate.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param channel        The channel that the mode operation applies to.
   * @param sourceNick     The nick of the user that set the mode.
   * @param sourceLogin    The login of the user that set the mode.
   * @param sourceHostname The hostname of the user that set the mode.
   * @param mode           The mode that has been set.
   */
  protected void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    super.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode)) break;
  }

  /**
   * Called when the mode of a user is set.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param targetNick     The nick that the mode operation applies to.
   * @param sourceNick     The nick of the user that set the mode.
   * @param sourceLogin    The login of the user that set the mode.
   * @param sourceHostname The hostname of the user that set the mode.
   * @param mode           The mode that has been set.
   * @since PircBot 1.2.0
   */
  protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    super.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onUserMode(targetNick, sourceNick, sourceLogin, sourceHostname, mode)) break;
  }

  /**
   * Called when we are invited to a channel by a user.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param targetNick     The nick of the user being invited - should be us!
   * @param sourceNick     The nick of the user that sent the invitation.
   * @param sourceLogin    The login of the user that sent the invitation.
   * @param sourceHostname The hostname of the user that sent the invitation.
   * @param channel        The channel that we're being invited to.
   * @since PircBot 0.9.5
   */
  protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
    super.onInvite(targetNick, sourceNick, sourceLogin, sourceHostname, channel);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onInvite(targetNick, sourceNick, sourceLogin, sourceHostname, channel)) break;
  }

  /**
   * This method is called whenever a DCC SEND request is sent to the PircBot.
   * This means that a client has requested to send a file to us.
   * This abstract implementation performs no action, which means that all
   * DCC SEND requests will be ignored by default. If you wish to receive
   * the file, then you may override this method and call the receive method
   * on the DccFileTransfer object, which connects to the sender and downloads
   * the file.
   * <p/>
   * Example:
   * <pre> public void onIncomingFileTransfer(DccFileTransfer transfer) {
   *     // Use the suggested file name.
   *     File file = transfer.getFile();
   *     // Receive the transfer and save it to the file, allowing resuming.
   *     transfer.receive(file, true);
   * }</pre>
   * <p/>
   * <b>Warning:</b> Receiving an incoming file transfer will cause a file
   * to be written to disk. Please ensure that you make adequate security
   * checks so that this file does not overwrite anything important!
   * <p/>
   * Each time a file is received, it happens within a new Thread
   * in order to allow multiple files to be downloaded by the PircBot
   * at the same time.
   * <p/>
   * If you allow resuming and the file already partly exists, it will
   * be appended to instead of overwritten.  If resuming is not enabled,
   * the file will be overwritten if it already exists.
   * <p/>
   * You can throttle the speed of the transfer by calling the setPacketDelay
   * method on the DccFileTransfer object, either before you receive the
   * file or at any moment during the transfer.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param transfer The DcccFileTransfer that you may accept.
   * @see org.jibble.pircbot.DccFileTransfer
   * @since PircBot 1.2.0
   */
  protected void onIncomingFileTransfer(DccFileTransfer transfer) {
    super.onIncomingFileTransfer(transfer);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onIncomingFileTransfer(transfer)) break;
  }

  /**
   * This method gets called when a DccFileTransfer has finished.
   * If there was a problem, the Exception will say what went wrong.
   * If the file was sent successfully, the Exception will be null.
   * <p/>
   * Both incoming and outgoing file transfers are passed to this method.
   * You can determine the type by calling the isIncoming or isOutgoing
   * methods on the DccFileTransfer object.
   *
   * @param transfer The DccFileTransfer that has finished.
   * @param e        null if the file was transfered successfully, otherwise this
   *                 will report what went wrong.
   * @see org.jibble.pircbot.DccFileTransfer
   * @since PircBot 1.2.0
   */
  protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
    super.onFileTransferFinished(transfer, e);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onFileTransferFinished(transfer, e)) break;
  }

  /**
   * This method will be called whenever a DCC Chat request is received.
   * This means that a client has requested to chat to us directly rather
   * than via the IRC server. This is useful for sending many lines of text
   * to and from the bot without having to worry about flooding the server
   * or any operators of the server being able to "spy" on what is being
   * said. This abstract implementation performs no action, which means
   * that all DCC CHAT requests will be ignored by default.
   * <p/>
   * If you wish to accept the connection, then you may override this
   * method and call the accept() method on the DccChat object, which
   * connects to the sender of the chat request and allows lines to be
   * sent to and from the bot.
   * <p/>
   * Your bot must be able to connect directly to the user that sent the
   * request.
   * <p/>
   * Example:
   * <pre> public void onIncomingChatRequest(DccChat chat) {
   *     try {
   *         // Accept all chat, whoever it's from.
   *         chat.accept();
   *         chat.sendLine("Hello");
   *         String response = chat.readLine();
   *         chat.close();
   *     }
   *     catch (IOException e) {}
   * }</pre>
   * <p/>
   * Each time this method is called, it is called from within a new Thread
   * so that multiple DCC CHAT sessions can run concurrently.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param chat A DccChat object that represents the incoming chat request.
   * @see org.jibble.pircbot.DccChat
   * @since PircBot 1.2.0
   */
  protected void onIncomingChatRequest(DccChat chat) {
    super.onIncomingChatRequest(chat);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onIncomingChatRequest(chat)) break;
  }

  /**
   * This method is called whenever we receive a VERSION request.
   * This abstract implementation responds with the PircBot's _version string,
   * so if you override this method, be sure to either mimic its functionality
   * or to call super.onVersion(...);
   *
   * @param sourceNick     The nick of the user that sent the VERSION request.
   * @param sourceLogin    The login of the user that sent the VERSION request.
   * @param sourceHostname The hostname of the user that sent the VERSION request.
   * @param target         The target of the VERSION request, be it our nick or a channel name.
   */
  protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
    super.onVersion(sourceNick, sourceLogin, sourceHostname, target);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onVersion(sourceNick, sourceLogin, sourceHostname, target)) break;
  }

  /**
   * This method is called whenever we receive a PING request from another
   * user.
   * <p/>
   * This abstract implementation responds correctly, so if you override this
   * method, be sure to either mimic its functionality or to call
   * super.onPing(...);
   *
   * @param sourceNick     The nick of the user that sent the PING request.
   * @param sourceLogin    The login of the user that sent the PING request.
   * @param sourceHostname The hostname of the user that sent the PING request.
   * @param target         The target of the PING request, be it our nick or a channel name.
   * @param pingValue      The value that was supplied as an argument to the PING command.
   */
  protected void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {
    super.onPing(sourceNick, sourceLogin, sourceHostname, target, pingValue);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onPing(sourceNick, sourceLogin, sourceHostname, target, pingValue)) break;
  }

  /**
   * The actions to perform when a PING request comes from the server.
   * <p/>
   * This sends back a correct response, so if you override this method,
   * be sure to either mimic its functionality or to call
   * super.onServerPing(response);
   *
   * @param response The response that should be given back in your PONG.
   */
  protected void onServerPing(String response) {
    super.onServerPing(response);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onServerPing(response)) break;
  }

  /**
   * This method is called whenever we receive a TIME request.
   * <p/>
   * This abstract implementation responds correctly, so if you override this
   * method, be sure to either mimic its functionality or to call
   * super.onTime(...);
   *
   * @param sourceNick     The nick of the user that sent the TIME request.
   * @param sourceLogin    The login of the user that sent the TIME request.
   * @param sourceHostname The hostname of the user that sent the TIME request.
   * @param target         The target of the TIME request, be it our nick or a channel name.
   */
  protected void onTime(String sourceNick, String sourceLogin, String sourceHostname, String target) {
    super.onTime(sourceNick, sourceLogin, sourceHostname, target);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onTime(sourceNick, sourceLogin, sourceHostname, target)) break;
  }

  /**
   * This method is called whenever we receive a FINGER request.
   * <p/>
   * This abstract implementation responds correctly, so if you override this
   * method, be sure to either mimic its functionality or to call
   * super.onFinger(...);
   *
   * @param sourceNick     The nick of the user that sent the FINGER request.
   * @param sourceLogin    The login of the user that sent the FINGER request.
   * @param sourceHostname The hostname of the user that sent the FINGER request.
   * @param target         The target of the FINGER request, be it our nick or a channel name.
   */
  protected void onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target) {
    super.onFinger(sourceNick, sourceLogin, sourceHostname, target);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onFinger(sourceNick, sourceLogin, sourceHostname, target)) break;
  }

  /**
   * This method is called whenever we receive a line from the server that
   * the PircBot has not been programmed to recognise.
   * <p/>
   * The implementation of this method in the PircBot abstract class
   * performs no actions and may be overridden as required.
   *
   * @param line The raw line that was received from the server.
   */
  protected void onUnknown(String line) {
    super.onUnknown(line);
    if (listeners == null) return;
    for (PircListener l : listeners) if (!l.onUnknown(line)) break;
  }
}

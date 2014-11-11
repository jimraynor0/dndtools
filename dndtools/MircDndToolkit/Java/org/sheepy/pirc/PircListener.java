package org.sheepy.pirc;

import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.User;

/**
 * Listener of Pirc event
 * @author Ho Yiu YEUNG
 * @since 2007 Mar 17
 */
public interface PircListener {
  boolean onAction(String sender, String login, String hostname, String target, String action);

  boolean onChannelInfo(String channel, int userCount, String topic);

  boolean onConnect();

//  boolean onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient);

//  boolean onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient);

  boolean onDisconnect();

  boolean onFileTransferFinished(DccFileTransfer transfer, Exception e);

  boolean onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target);

  boolean onIncomingChatRequest(DccChat chat);

  boolean onIncomingFileTransfer(DccFileTransfer transfer);

  boolean onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel);

  boolean onJoin(String channel, String sender, String login, String hostname);

  boolean onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason);

  boolean onMessage(String channel, String sender, String login, String hostname, String message);

  boolean onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode);

  boolean onNickChange(String oldNick, String login, String hostname, String newNick);

  boolean onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice);

//  boolean onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient);

  boolean onPart(String channel, String sender, String login, String hostname);

  boolean onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue);

  boolean onPrivateMessage(String sender, String login, String hostname, String message);

  boolean onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason);

  boolean onServerPing(String response);

  boolean onServerResponse(int code, String response);

  boolean onTime(String sourceNick, String sourceLogin, String sourceHostname, String target);

  boolean onTopic(String channel, String topic, String setBy, long date, boolean changed);

  boolean onUnknown(String line);

  boolean onUserList(String channel, User[] users);

  boolean onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode);

  boolean onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target);

//  boolean onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient);
}

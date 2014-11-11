package org.sheepy.pirc;

import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.User;

/**
 * An empty implementation of PircListener
 * @author Ho Yiu YEUNG
 * @since 2007 Mar 17
 */
public class PircAdapter implements PircListener {

  public boolean onAction(String sender, String login, String hostname, String target, String action) {
    return true;
  }

  public boolean onChannelInfo(String channel, int userCount, String topic) {
    return true;
  }

  public boolean onConnect() {
    return true;
  }

  public boolean onDisconnect() {
    return true;
  }

  public boolean onFileTransferFinished(DccFileTransfer transfer, Exception e) {
    return true;
  }

  public boolean onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target) {
    return true;
  }

  public boolean onIncomingChatRequest(DccChat chat) {
    return true;
  }

  public boolean onIncomingFileTransfer(DccFileTransfer transfer) {
    return true;
  }

  public boolean onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
    return true;
  }

  public boolean onJoin(String channel, String sender, String login, String hostname) {
    return true;
  }

  public boolean onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    return true;
  }

  public boolean onMessage(String channel, String sender, String login, String hostname, String message) {
    return true;
  }

  public boolean onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    return true;
  }

  public boolean onNickChange(String oldNick, String login, String hostname, String newNick) {
    return true;
  }

  public boolean onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
    return true;
  }

  public boolean onPart(String channel, String sender, String login, String hostname) {
    return true;
  }

  public boolean onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {
    return true;
  }

  public boolean onPrivateMessage(String sender, String login, String hostname, String message) {
    return true;
  }

  public boolean onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    return true;
  }

  public boolean onServerPing(String response) {
    return true;
  }

  public boolean onServerResponse(int code, String response) {
    return true;
  }

  public boolean onTime(String sourceNick, String sourceLogin, String sourceHostname, String target) {
    return true;
  }

  public boolean onTopic(String channel, String topic, String setBy, long date, boolean changed) {
    return true;
  }

  public boolean onUnknown(String line) {
    return true;
  }

  public boolean onUserList(String channel, User[] users) {
    return true;
  }

  public boolean onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    return true;
  }

  public boolean onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
    return true;
  }

}
package dndbot.module;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jibble.pircbot.ReplyConstants;
import org.jibble.pircbot.User;
import org.sheepy.util.StringUtil;

import dndbot.irc.IRC;

/**
 * Channel module, handles commands related to channels.
 *
 * Created by Ho Yiu YEUNG on Apr 2, 2007 at 7:39:08 PM
 */
public class Channel extends AbstractBotModule {
  /**
   * Person who last send an invitation
   */
  private String lastInvite;
  private Map<String, String> invitor;
  private Map<String, String> invitor_notice = new HashMap<String, String>();

  public Channel(IRC irc) {
    super(irc);
    irc.addListener(this);
    invitor = (Map) irc.data.get("channel_invitor");
    if (invitor == null) {
      invitor = new HashMap<String, String>();
      irc.data.put("channel_invitor", invitor);
    }
  }

  public boolean isAlwaysOn() { return true; }

  public void reset() {
    irc.removeListener(this);
    super.reset();
  }


  public String getCommandPattern() {
    return "here|auto-op";
  }

  public String[] getCommand() {
    return new String[]{"here", "auto-op"};
  }


  public String getDirectCommandPattern() {
    return getCommandPattern()+"|join|part";
  }

  public String[] getDirectCommand() {
    return new String[]{"here", "join", "part", "auto-op"};
  }

  public void onCommand(ModuleEvent evt) {
    final boolean is_admin = irc.is_admin(evt.sender);
    evt.processed = true;

      // here
    if (evt.command.equals("here")) {
      if (evt.parameter.length() > 0) {
        irc.settings.put(evt.sender, "output", evt.parameter);
        evt.sendMessage(evt.sender, MessageFormat.format(res.getString("outputSet"), evt.parameter));
      } else if (!StringUtil.isEmpty(evt.channel)) {
        irc.settings.put(evt.sender, "output", evt.channel);
        evt.sendMessage(evt.sender, MessageFormat.format(res.getString("outputSet"), evt.channel));
      } else {
        irc.settings.put(evt.sender, "output", null);
        evt.sendMessage(evt.sender, res.getString("outputUnset"));
      }

      // join
    } else if (evt.command.equals("join")) {
      String channel = evt.parameter;
      if (channel.length() > 0) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        joinChannel(channel, evt.login, evt.sender);
      }

      // part
    } else if (evt.command.equals("part")) {
      String channel = evt.parameter;
      if (channel.length() > 0) {
        if (!channel.startsWith("#")) channel = "#" + channel;
        /*
        if (!is_admin && !irc.is_channel_admin(evt.sender, channel)) {
          evt.sendMessage(evt.getLocation(), res.getString("errNotOp"));
          return;
        }
        */
        lastInvite = evt.sender;
        irc.partChannel(channel);
      }

      // auto-op
    } else if (evt.command.equals("auto-op")) {
      String target = evt.channel;
      String notification = null;
      if (target == null && evt.parameter.length() > 0 ) target = evt.parameter;
      if (target == null) {
        // Global auto-op
        if (!is_admin) {
          evt.sendMessage(evt.getLocation(), res.getString("errNotAdmin"));
          return;
        }
        target = "#";
        notification = evt.sender;
      } else {
        // Channel auto-op
        if (!target.startsWith("#")) target = "#" + target;
        String[] channels = irc.getChannels();
        Arrays.sort(channels);
        if (Arrays.binarySearch(channels, target) < 0) {
          evt.sendMessage(evt.sender, res.getString("errNotInChannel"));
          return;
        }
        if (!is_admin && !irc.is_channel_admin(evt.sender, target)) {
          evt.sendMessage(evt.getLocation(), res.getString("errNotOp"));
          return;
        }
        notification = target;
      }
      // Update setting and send notification
      if (irc.settings.get(target, "auto-op") == null) {
        irc.settings.put(target, "auto-op", "true");
        evt.sendMessage(notification, target.equals("#") ? res.getString("auto-op_all_on") : res.getString("auto-op_on"));
      } else {
        if ("true".equals(irc.settings.get(target, "auto-op"))) {
          irc.settings.put(target, "auto-op", target.equals("#") ? null : "false");
          evt.sendMessage(notification, target.equals("#") ? res.getString("auto-op_all_off") : res.getString("auto-op_off"));
        } else {
          irc.settings.put(target, "auto-op", null);
          evt.sendMessage(notification, res.getString("auto-op_none"));
        }
      }

    } else {
      evt.processed = false;
    }
  }

  /** Join channel on invite */
  public boolean onInvite(String targetNick, String sourceNick, String login, String hostname, String channel) {
    if (targetNick.equalsIgnoreCase(irc.getNick())) joinChannel(channel, login, sourceNick);
    return super.onInvite(targetNick, sourceNick, login, hostname, channel);
  }

  private void joinChannel(String channel, String login, String sourceNick) {
    if (irc.getChannels().length >= 9) irc.departIdleChannel(10);
    else if (irc.getChannels().length >= 8) irc.departIdleChannel(30);
    else if (irc.getChannels().length >= 6) irc.departIdleChannel(180);
//    else if (irc.getChannels().length >= 3) irc.departIdleChannel(300);
    invitor.put(channel, login);
    invitor_notice.put(channel, sourceNick);
    lastInvite = sourceNick;
    irc.joinChannel(channel);
  }

  /** Check parting / op condition on someone part. */
  public boolean onPart(String channel, String sender, String login, String hostname) {
    if (!sender.equalsIgnoreCase(irc.getNick()))
      partIfAlone(channel);
    return super.onPart(channel, sender, login, hostname);
  }

  /** Check parting / op condition on someone being kicked. */
  public boolean onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    if (!recipientNick.equalsIgnoreCase(irc.getNick()))
      partIfAlone(channel);
    return super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
  }

  /** Check parting / op condition on someone quit. */
  public boolean onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    if (!sourceNick.equalsIgnoreCase(irc.getNick()))
      for (String channel : irc.getChannels())
        partIfAlone(channel);
    return super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
  }

  /** Check auto-op and op condition on someone join. */
  public boolean onJoin(String channel, String sender, String login, String hostname) {
    if (sender.equalsIgnoreCase(irc.getNick())) {
      String inv = invitor_notice.get(channel);
      if (inv != null && !inv.equals(IRC.USER_GUI)) {
// Invitor announcement.  A bit annoying, esp. in middle of a campaign.  Just personal opinion, no complains received.
//        irc.sendAction(channel, MessageFormat.format(res.getString("invited"), inv));
        invitor_notice.remove(channel);
      }
    }  else {
      String channelAutoOp = irc.settings.get(channel, "auto-op");
      if ("true".equals(channelAutoOp) || (!"false".equals(channelAutoOp) && "true".equals(irc.settings.get("#", "auto-op"))))
        irc.op(channel, sender);
      else
        opIfAlone(channel);
    }
    return super.onJoin(channel, sender, login, hostname);
  }

  /** Check op condition on someone change mode. */
  public boolean onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    opIfAlone(channel);
    return super.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
  }

  /**
   * Send response on error
   */
  public boolean onServerResponse(int code, String response) {
    if (code == ReplyConstants.ERR_TOOMANYCHANNELS) {
      // Joined too many channels
      irc.departIdleChannel(10);
      if (lastInvite != null) irc.sendMessage(lastInvite, res.getString("errTooManyChannel"));
      lastInvite = null;
    }
    return super.onServerResponse(code, response);
  }

  /**
   * Part given channel if we are alone
   *
   * @param channel
   */
  public void partIfAlone(String channel) {
    User[] users = irc.getUsers(channel);
    if (users.length == 1) irc.partChannel(channel);
    else opIfAlone(channel);
    // Check for robots, e.g. Dicebot or oicebot
    for (User u : users) {
      if (!u.getNick().toLowerCase().endsWith("bot"))
        return; // Not all users are bot; exit
    }
    irc.partChannel(channel);
  }

  /**
   * Op everyone if we are the only op
   *
   * @param channel
   */
  public void opIfAlone(String channel) {
    User[] users = irc.getUsers(channel);
    int op_count = 0;
    for (User u : users) {
      if (u.getNick().equalsIgnoreCase(irc.getNick())) {
        if (!u.isOp()) return;
      } else {
        if (u.isOp()) {
          op_count++;
        }
      }
    }
    if (op_count <= 0)
      for (User u : users) irc.op(channel, u.getNick());
  }

}

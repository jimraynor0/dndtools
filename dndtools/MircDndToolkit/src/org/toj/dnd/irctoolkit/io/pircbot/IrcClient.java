package org.toj.dnd.irctoolkit.io.pircbot;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.toj.dnd.irctoolkit.configs.GlobalConfigs;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.Command;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class IrcClient extends PircBot {

    private static final String IRC_SERVER_HOST = "irc.ourirc.com";
    private static final IrcClient INSTANCE = new IrcClient();

    public static IrcClient getInstance() {
        return INSTANCE;
    }

    private Logger log = Logger.getLogger(this.getClass());
    private String topicCache;

    private IrcClient() {
        this.setMessageDelay(10);
        this.setVerbose(false);
        this.setName(GlobalConfigs.getConfigs().get(
                GlobalConfigs.CONF_IRC_NICKNAME));
    }

    public void connect() throws NickAlreadyInUseException, IOException,
            IrcException {
        String host = GlobalConfigs.getConfigs().get(
                GlobalConfigs.CONF_IRC_SERV_HOST);
        if (host == null) {
            host = IRC_SERVER_HOST;
        }
        String portStr = GlobalConfigs.getConfigs().get(
                GlobalConfigs.CONF_IRC_SERV_PORT);
        int port = -1;
        try {
            port = Integer.parseInt(portStr);
        } catch (Exception e) {
            // remain -1
        }
        try {
            if (port >= 0) {
                log.debug("connecting to " + host + ":" + port);
                this.connect(host, port);
            } else {
                log.debug("connecting to " + host);
                this.connect(host);
            }
        } catch (NickAlreadyInUseException e) {
            this.setName(GlobalConfigs.getConfigs().get(
                    GlobalConfigs.CONF_IRC_NICKNAME_ALT));
            if (port >= 0) {
                this.connect(host, port);
            } else {
                this.connect(host);
            }
        }
        log.debug("connected");
    }

    @Override
    protected void onMessage(String channel, String sender, String login,
            String hostname, String msg) {
        String cmd = Colors.removeFormattingAndColors(msg);
        if (log.isDebugEnabled()) {
            log.debug(sender + " sent public message in " + channel + ": "
                    + cmd);
        }
        if (cmd.startsWith(".") || sender.equals("Dicebot")
                || sender.equals("Oicebot")) {
            if (log.isDebugEnabled()) {
                log.debug("Command received: " + cmd);
            }
            String actor = sender;
            if (getGame() != null && getGame().hasAliases(sender)) {
                actor = getGame().mapAlias(sender);
                if (log.isDebugEnabled()) {
                    log.debug("alias mapped: " + sender + " -> " + actor);
                }
            }
            try {
                Command c = IrcCommandFactory.buildCommand(
                        new StringBuilder(cmd).append(" ").append(channel)
                                .append(" ").append(actor).toString(), null, 0);
                if (c == null) {
                    log.warn("Command dropped: invalid command.");
                } else {
                    ToolkitEngine.getEngine().queueCommand(c);
                }
            } catch (Exception e) {
                log.warn("Exception while parsing cmd: ", e);
            }
        }
    }

    private Game getGame() {
        return ToolkitEngine.getEngine().getContext().getGame();
    }

    @Override
    protected void onPrivateMessage(String sender, String login,
            String hostname, String msg) {
        String cmd = Colors.removeFormattingAndColors(msg);
        if (log.isDebugEnabled()) {
            log.debug(sender + " sent private message: " + cmd);
        }
        if (cmd.startsWith(".") || sender.equals("Dicebot")
                || sender.equals("Oicebot")) {
            if (log.isDebugEnabled()) {
                log.debug("Command received: " + cmd);
            }
            String actor = sender;
            if (getGame() != null && getGame().hasAliases(sender)) {
                actor = getGame().mapAlias(sender);
                if (log.isDebugEnabled()) {
                    log.debug("alias mapped: " + sender + " -> " + actor);
                }
            }
            try {
                Command c = IrcCommandFactory.buildCommand(
                        new StringBuilder(cmd).append(" ").append(sender)
                                .append(" ").append(actor).toString(), null, 0);
                if (c == null) {
                    log.warn("Command dropped: invalid command.");
                } else {
                    ToolkitEngine.getEngine().queueCommand(c);
                }
            } catch (Exception e) {
                log.warn("Exception while parsing cmd: ", e);
            }
        }
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin,
            String sourceHostname, String target, String notice) {
        log.debug(sourceNick + " sent a notice: " + notice);
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname,
            String newNick) {
        log.debug(oldNick + " changed his/her nick to " + newNick);
    }

    @Override
    protected void onInvite(String targetNick, String sourceNick,
            String sourceLogin, String sourceHostname, String channel) {
        super.joinChannel(channel);
        super.sendAction(channel, this.getName() + "奉" + Colors.RED
                + sourceNick + Colors.PURPLE + "的召唤而来");
    }

    @Override
    protected void onAction(String sender, String login, String hostname,
            String target, String action) {
        if (("dismiss " + this.getName()).equalsIgnoreCase(action)) {
            super.partChannel(target, Colors.PURPLE + sender
                    + Colors.DARK_GREEN + "，我还会回来的！");
        }
    }

    public void processOutgoingMsgs(List<OutgoingMsg> msgs) {
        if (msgs != null) {
            for (OutgoingMsg msg : msgs) {
                if (msg.getWriteTo().equals(OutgoingMsg.WRITE_TO_MSG)) {
                    super.sendMessage(msg.getChan(), msg.getContent());
                } else if (msg.getWriteTo().equals(OutgoingMsg.WRITE_TO_TOPIC)) {
                    this.topicCache = msg.getContent();
                } else if (msg.getWriteTo().equals(
                        OutgoingMsg.REFRESH_TOPIC_NOTICE)) {
                    super.setTopic(msg.getChan(), topicCache);
                }
            }
        }
    }

    public void send(OutgoingMsg msg) {
        super.sendMessage(msg.getChan(), msg.getContent());
    }

    public String getNickname() {
        if (isConnected()) {
            return getNick();
        } else {
            return getName();
        }
    }

    public void setNickname(String nick) {
        if (isConnected()) {
            changeNick(nick);
            ToolkitEngine.getEngine().setConnected();
        } else {
            setName(nick);
        }
    }

    @Override
    protected void onConnect() {
        super.onConnect();
        ToolkitEngine.getEngine().setConnected();
    }

    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        ToolkitEngine.getEngine().setDisconnected();
    }
}

package org.toj.dnd.irctoolkit.engine.command;

import java.net.InetAddress;
import java.util.List;

import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

/**
 * Command format: <chan> <nick> <command> todo list: automatically end
 * EONT/SONT effects; automatically make save(save bonus); auto resolve dot;
 * aftereffect; add team tag on Char(ally|enemy); restore from existing title;
 * save title with a specified name / retrieve previous title by name;
 * undo/redo(might still have problem.); subclass Command; concurrent exception;
 * move undo/redo to HistoryManager use maven
 * 
 * @author zhaijim
 */
public abstract class GameCommand extends Command {

    protected InetAddress incomingAddr;
    protected int incomingPort;
    protected String chan;
    protected String caller;
    protected boolean topicRefreshNeeded;

    @Override
    public abstract List<OutgoingMsg> execute() throws ToolkitCommandException;

    public boolean topicRefreshNeeded() {
        return topicRefreshNeeded;
    }

    @Override
    public boolean updatesTopic() {
        return true;
    }

    @Override
    public boolean updatesMap() {
        return false;
    }

    @Override
    public boolean undoable() {
        return false;
    }

    @Override
    public void undo() throws ToolkitCommandException {
        return;
    }

    @Override
    protected Game getGame() {
        return context.getGame();
    }

    protected boolean isFromDm() {
        return getGame().isDm(caller);
    }

    protected void sendMsg(String content) {
        msgs.add(new OutgoingMsg(chan, caller, content,
                OutgoingMsg.WRITE_TO_MSG, incomingAddr, incomingPort));
    }

    protected void sendMsgToDefaultChan(String content) {
        msgs.add(new OutgoingMsg(getGame().getOutputChannel(), caller, content,
                OutgoingMsg.WRITE_TO_MSG, incomingAddr, incomingPort));
    }

    protected void sendTopic(String content) {
        msgs.add(new OutgoingMsg(getGame().getOutputChannel(), caller, content,
                OutgoingMsg.WRITE_TO_TOPIC, incomingAddr, incomingPort));
    }

    protected void refreshTopic() {
        sendTopic(getGame().generateTopic());
        msgs.add(new OutgoingMsg(getGame().getOutputChannel(), caller, null,
                OutgoingMsg.REFRESH_TOPIC_NOTICE, incomingAddr, incomingPort));
    }

    public void setTopicRefreshNeeded(boolean topicRefreshNeeded) {
        this.topicRefreshNeeded = topicRefreshNeeded;
    }

    public boolean requireGameContext() {
        return true;
    }

    protected String composite(Object[] parts) {
        StringBuilder sb = new StringBuilder();
        for (Object str : parts) {
            if (!str.equals(parts[0])) {
                sb.append(" ");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }
}

package org.toj.dnd.irctoolkit.history;

import java.util.Stack;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.engine.command.Command;
import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class HistoryManager {

    private Stack<Command> ircCmdHistory = new Stack<Command>();
    private Stack<Command> uiCmdHistory = new Stack<Command>();
    private Stack<Object> gameHistory = new Stack<Object>();
    private Stack<Element> mapHistory = new Stack<Element>();

    public void saveGameSnapshot(Object snapshot) {
        gameHistory.push(snapshot);
    }

    public Object retrievePreviousGame() {
        return gameHistory.pop();
    }

    public void saveMapSnapshot(Element snapshot) {
        mapHistory.push(snapshot);
    }

    public Element retrievePreviousMap() {
        return mapHistory.pop();
    }

    public void pushCmd(Command cmd) {
        if (cmd instanceof MapCommand) {
            uiCmdHistory.push(cmd);
        } else {
            ircCmdHistory.push(cmd);
        }
    }

    public void undoIrcCmd(String chan) throws ToolkitCommandException {
        if (!ircCmdHistory.isEmpty()) {
            ircCmdHistory.pop().undo();
        }
    }

    public void undoUiCmd() throws ToolkitCommandException {
        if (!uiCmdHistory.isEmpty()) {
            uiCmdHistory.pop().undo();
        }
    }
}

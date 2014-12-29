package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.Map;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "listalias", args = {})
public class ListAliasCommand extends UndoableTopicCommand {

    public ListAliasCommand(Object[] args) {
        super();
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        Map<String, String> map = getGame().getAliases();
        for (String alias : map.keySet()) {
            sendMsg(alias + " => " + map.get(alias));
        }
    }
}

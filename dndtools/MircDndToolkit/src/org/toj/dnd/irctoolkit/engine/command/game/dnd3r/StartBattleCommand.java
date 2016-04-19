package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.game.common.RefreshTopicCommand;

@IrcCommand(command = "startbattle", args = {})
public class StartBattleCommand extends Dnd3rGameCommand {

    public StartBattleCommand(Object[] args) {
    }

    @Override
    public void doProcess() {
        getGame().startBattle();

        List<String> needsInitRolled = new ArrayList<String>(getGame().getPcs()
                .size());
        for (String pcName : getGame().getPcs().keySet()) {
            if (getGame().getBattle().findCharByName(pcName) == null) {
                needsInitRolled.add(pcName);
            }
        }
        if (!needsInitRolled.isEmpty()) {
            Object[] args = new Object[needsInitRolled.size() + 1];
            args[0] = null;
            System.arraycopy(needsInitRolled.toArray(), 0, args, 1,
                    needsInitRolled.size());
            ToolkitEngine.getEngine().queueCommand(new InitCommand(args));
            ToolkitEngine.getEngine().queueCommand(new RefreshTopicCommand());
        }
    }
}

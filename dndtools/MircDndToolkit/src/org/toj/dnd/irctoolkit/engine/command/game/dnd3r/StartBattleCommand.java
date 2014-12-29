package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.game.common.RefreshTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.ui.LoadMapFromFileCommand;
import org.toj.dnd.irctoolkit.game.dnd3r.encounter.Encounter;
import org.toj.dnd.irctoolkit.io.file.GameStore;

@IrcCommand(command = "startbattle", args = { CommandSegment.NULLABLE_LIST })
public class StartBattleCommand extends Dnd3rGameCommand {

    private String encounterName;

    public StartBattleCommand(Object[] args) {
        if (args != null && args.length > 0) {
            this.encounterName =
                StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
        }
    }

    @Override
    public void doProcess() {
        if (encounterName != null) {
            Encounter encounter;
            try {
                encounter = GameStore.loadEncounter(encounterName);
            } catch (IOException e) {
                sendMsg("Failed to load encounter " + encounterName);
                return;
            }
            if (encounterName != null && encounter == null) {
                sendMsg("Encounter " + encounterName + " is not found.");
                return;
            }
            if (encounter == null) {
                getGame().startBattle();
            } else {
                if (encounter.mapName != null) {
                    ToolkitEngine.getEngine()
                        .queueCommand(
                            new LoadMapFromFileCommand(new File(
                                encounter.mapName)));
                }
                getGame().startEncounter(encounter);
            }
        } else {
            getGame().startBattle();
        }

        List<String> needsInitRolled =
            new ArrayList<String>(getGame().getPcs().size()
                + getGame().getNpcs().size());
        for (String pcName : getGame().getPcs().keySet()) {
            if (getGame().getBattle().findCharByName(pcName) == null) {
                needsInitRolled.add(pcName);
            }
        }
        for (String npcName : getGame().getNpcs().keySet()) {
            if (getGame().getBattle().findCharByName(npcName) == null) {
                needsInitRolled.add(npcName);
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

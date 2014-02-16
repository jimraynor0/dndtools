package org.toj.dnd.irctoolkit.engine.command.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.map.LoadMapFromFileCommand;
import org.toj.dnd.irctoolkit.game.encounter.Encounter;
import org.toj.dnd.irctoolkit.io.file.GameStore;

@IrcCommand(command="startbattle", args = {CommandSegment.LIST})
public class StartBattleCommand extends UndoableTopicCommand {

    private String encounterName;

    public StartBattleCommand(Object[] args) {
        this.encounterName = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
    }

    @Override
    public void doProcess() {
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
        List<String> needsInitRolled = new ArrayList<String>(getGame().getPcs()
                .size() + getGame().getNpcs().size());
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
            ToolkitEngine.getEngine().queueCommand(
                    new InitCommand(needsInitRolled
                            .toArray(new String[needsInitRolled.size()])));
        }
    }
}

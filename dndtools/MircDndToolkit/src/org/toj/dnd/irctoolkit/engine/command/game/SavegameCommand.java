package org.toj.dnd.irctoolkit.engine.command.game;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "savegame", args = { CommandSegment.STRING })
public class SavegameCommand extends GameCommand {

    private Logger log = Logger.getLogger(this.getClass());
    private String name;

    public SavegameCommand(Object[] args) {
        this((String) args[0]);
    }

    public SavegameCommand(String name) {
        this.name = name;
    }

    @Override
    public List<OutgoingMsg> execute() {
        try {
            GameStore.save(context.getGame());
        } catch (IOException e) {
            log.error("Error while loading: ", e);
            sendMsg("Failed to save game under name: " + name);
        }
        sendMsg("Game saved under name: " + name);
        ToolkitEngine.getEngine().queueCommand(new CreateOrLoadCommand(name));
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

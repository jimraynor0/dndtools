package org.toj.dnd.irctoolkit.engine.command.game;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "startgame", args = { CommandSegment.STRING })
public class CreateOrLoadCommand extends GameCommand {

    private Logger log = Logger.getLogger(this.getClass());
    private String name;

    public CreateOrLoadCommand(Object[] args) {
        this((String) args[0]);
    }

    public CreateOrLoadCommand(String name) {
        this.name = name;
    }

    @Override
    public List<OutgoingMsg> execute() {
        Game game = null;
        try {
            game = GameStore.loadGame(name);
        } catch (IOException e) {
            log.error("Error while loading: ", e);
        }
        if (game == null) {
            game = GameStore.createGame(name);
            context.setGame(game);
            sendMsg("Game created.");
            log.debug("Game created: " + name);
        } else {
            context.setGame(game);
            sendTopic(game.generateTopic());
            refreshTopic();
            sendMsg("Game loaded.");
            log.debug("Game loaded: " + name);
        }
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

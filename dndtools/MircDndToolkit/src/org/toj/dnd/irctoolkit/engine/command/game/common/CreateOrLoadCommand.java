package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "startgame", args = { CommandSegment.STRING, CommandSegment.NULLABLE_STRING })
public class CreateOrLoadCommand extends GameCommand {

    private Logger log = Logger.getLogger(this.getClass());
    private String name;
    private String ruleSet;

    public CreateOrLoadCommand(Object[] args) {
        this((String) args[0]);
        if (args.length == 2) {
            ruleSet = (String) args[1];
        }
    }

    public CreateOrLoadCommand(String name) {
        this.name = name;
        this.ruleSet = "dnd3r";
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
            game = GameStore.createGame(name, ruleSet);
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
        IrcCommandFactory.loadGameCommands(game);
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

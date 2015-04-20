package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "putback", args = { CommandSegment.STRING })
public class ReturnToDeckCommand extends DracaGameCommand {

    private String card;

    public ReturnToDeckCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().move(card, getPcZoneName(caller, Zone.HAND), Zone.DECK);
        ToolkitEngine.getEngine().queueCommand(new ShuffleDeckCommand(null));
        sendMsg(caller + "把一张牌放回了牌库");
    }
}

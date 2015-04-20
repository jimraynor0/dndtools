package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "discard", args = { CommandSegment.STRING })
public class DiscardCardCommand extends DracaGameCommand {

    private String card;

    public DiscardCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().move(card, getPcZoneName(caller, Zone.HAND), Zone.DISCARD);
        sendMsg(caller + "丢弃了" + card);
    }
}

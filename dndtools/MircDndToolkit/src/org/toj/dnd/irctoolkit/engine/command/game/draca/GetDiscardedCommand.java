package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "getdiscarded", args = { CommandSegment.STRING })
public class GetDiscardedCommand extends DracaGameCommand {

    private String card;

    public GetDiscardedCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().move(card, Zone.DISCARD, getPcZoneName(caller, Zone.HAND));
        sendMsg(caller + "从弃牌堆里拿回了" + card);
    }
}

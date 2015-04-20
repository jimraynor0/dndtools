package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "play", args = { CommandSegment.STRING })
public class PlayCardCommand extends DracaGameCommand {

    private String card;

    public PlayCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().move(card, getPcZoneName(caller, Zone.HAND), Zone.DISCARD);
        sendMsg(caller + "打出一张" + card);
    }
}

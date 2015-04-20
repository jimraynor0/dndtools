package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "getdiscarded", args = { CommandSegment.STRING })
public class GetDiscardedCommand extends DracaGameCommand {

    private String card;

    public GetDiscardedCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException,
            ToolkitWarningException {
        try {
            getGame()
                    .move(card, Zone.DISCARD, getPcZoneName(caller, Zone.HAND));
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("弃牌堆里没有" + card);
            }
        }
        sendMsg(caller + "从弃牌堆里拿回了" + card);
    }
}

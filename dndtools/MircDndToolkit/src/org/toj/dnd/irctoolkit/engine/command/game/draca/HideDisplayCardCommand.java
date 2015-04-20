package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "hide", args = { CommandSegment.STRING })
public class HideDisplayCardCommand extends UndoableDracaGameCommand {

    private String card;

    public HideDisplayCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException,
            ToolkitWarningException {
        try {
            getGame().move(card, getPcZoneName(caller, Zone.DISPLAY),
                    getPcZoneName(caller, Zone.HAND));
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("弃牌堆里没有" + card);
            }
        }
        sendMsg(caller + "从展示区拿回了" + card);
    }
}

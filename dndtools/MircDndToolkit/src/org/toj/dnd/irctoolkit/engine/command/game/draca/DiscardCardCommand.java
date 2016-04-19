package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "discard", args = { CommandSegment.STRING }, summary = ".discard 牌名 - 丢弃指定手牌。")
public class DiscardCardCommand extends UndoableDracaGameCommand {

    private String card;

    public DiscardCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException, ToolkitWarningException {
        try {
            getGame().move(card, getPcZoneName(caller, Zone.HAND), Zone.DISCARD);
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("你的手牌里没有" + card);
            }
        }
        sendMsg(caller + "丢弃了" + card);
    }
}

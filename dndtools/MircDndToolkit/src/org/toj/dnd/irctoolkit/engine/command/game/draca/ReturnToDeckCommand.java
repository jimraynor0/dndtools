package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "putback", args = { CommandSegment.STRING }, summary = ".putback 牌名 - 将指定手牌放回牌库并洗牌。")
public class ReturnToDeckCommand extends UndoableDracaGameCommand {

    private String card;

    public ReturnToDeckCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException,
            ToolkitWarningException {
        try {
            getGame().move(card, getPcZoneName(caller, Zone.HAND), Zone.DECK);
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("你的手牌里没有" + card);
            }
        }
        ToolkitEngine.getEngine().queueCommand(new ShuffleDeckCommand(null));
        sendMsg(caller + "把一张牌放回了牌库");
    }
}

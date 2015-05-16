package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "unlock", args = { CommandSegment.STRING }, summary = ".lock 牌名 - 将扣在桌面上的一张牌收回手中。")
public class UnlockCardCommand extends UndoableDracaGameCommand {

    private String card;

    public UnlockCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException,
            ToolkitWarningException {
        try {
            getGame().move(card, getPcZoneName(caller, Zone.LOCKED),
                    getPcZoneName(caller, Zone.HAND));
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("你扣下的牌里没有" + card);
            }
        }
        sendMsgToDefaultChan(caller + "拿回了扣下的一张牌。");
    }
}

package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "lock", args = { CommandSegment.STRING }, summary = ".lock 牌名 - 将指定手牌扣在桌面上(将指定的手牌移动到锁定区)，以示暂时无法使用。建议在小窗中使用。")
public class LockCardCommand extends UndoableDracaGameCommand {

    private String card;

    public LockCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException,
            ToolkitWarningException {
        try {
            getGame().move(card, getPcZoneName(caller, Zone.HAND),
                    getPcZoneName(caller, Zone.LOCKED));
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("你的手牌里没有" + card);
            }
        }
        sendMsgToDefaultChan(caller + "扣下了一张牌。");
    }
}

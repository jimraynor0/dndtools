package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "lay", args = { CommandSegment.STRING }, summary = ".lay 牌名 - 牌面朝下打出一张手牌，DM可以使用.flip掀开所有暗出的牌。建议在小窗中使用。")
public class PlayFaceDownCommand extends UndoableDracaGameCommand {

    private String card;

    public PlayFaceDownCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException,
            ToolkitWarningException {
        try {
            getGame()
                    .move(card, getPcZoneName(caller, Zone.HAND), getPcZoneName(caller, Zone.FACE_DOWN));
        } catch (ToolkitWarningException e) {
            if (DracaGame.CARD_DOESNOT_EXIST_IN_ZONE.equals(e.getMessage())) {
                throw new ToolkitWarningException("你的手牌里没有" + card);
            }
        }
        sendMsgToDefaultChan(caller + "暗出了一张牌");
    }
}

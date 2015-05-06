package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "draw", args = { CommandSegment.NULLABLE_INT },
        summary = ".draw <抓牌数量> - 抓牌。用数字指定抓牌数量，若不指定则默认抓1张。抓到的牌和当前的手牌会在小窗中显示。")
public class DrawCardCommand extends UndoableDracaGameCommand {

    private int amount = 1;

    public DrawCardCommand(Object[] args) {
        if (args.length > 0) {
            amount = (Integer) args[0];
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException, ToolkitWarningException {
        List<String> cards = getGame().draw(caller, amount);
        sendMsg(caller + "抓了" + amount + "张牌，牌库还剩下" + getGame().getDeck().size() + "张牌");
        whisper(caller, "你抓到了" + cards);
        whisper(caller, "你的手牌: " + getGame().getPcHand(caller).toText());
    }
}

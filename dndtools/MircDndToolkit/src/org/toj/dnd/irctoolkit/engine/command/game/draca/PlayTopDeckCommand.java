package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "topdeck", args = { CommandSegment.NULLABLE_INT },
        summary = ".topdeck <数量> - DM专用。从牌库顶翻开指定数量的牌，并在展示之后移入弃牌堆。如不指定数量默认翻开一张。")
public class PlayTopDeckCommand extends UndoableDracaGameCommand {

    private int amount = 1;

    public PlayTopDeckCommand(Object[] args) {
        if (args.length > 0) {
            amount = (Integer) args[0];
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException, ToolkitWarningException {
        if (!isFromDm()) {
            sendMsg("只有DM可以从牌库顶翻牌。");
            return;
        }
        List<String> cards = getGame().playTopDeck(amount);
        sendMsg(caller + "从牌库顶翻了" + amount + "张牌：" + cards);
    }
}

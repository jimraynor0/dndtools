package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "reset", args = {}, summary = ".reset - DM专用。将PC的手牌、弃牌堆以及处于所有其他区域的牌收回到牌库并洗牌。")
public class ResetDeckCommand extends UndoableDracaGameCommand {

    public ResetDeckCommand(Object[] args) {
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (!isFromDm()) {
            sendMsg("只有DM可以重置牌库。");
            return;
        }
        getGame().reset();
        sendMsg("牌库已被重置。");
    }
}

package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.PC;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "showboard", args = {})
public class ShowBoardCommand extends DracaGameCommand {

    public ShowBoardCommand(Object[] args) {
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        for (PC pc : getGame().getPcs().values()) {
            StringBuilder sb = new StringBuilder(pc.getName());
            sb.append("手牌").append(getGame().getPcHand(pc.getName()).size())
                    .append("张");
            if (!getGame().getPcDisplay(pc.getName()).isEmpty()) {
                sb.append("，展示区: ").append(
                        getGame().getPcDisplay(pc.getName()).toText());
            }
            sendMsg(sb.toString());
        }
        sendMsg("弃牌堆: " + getGame().getZone(Zone.DISCARD).getCards().toString());
    }
}

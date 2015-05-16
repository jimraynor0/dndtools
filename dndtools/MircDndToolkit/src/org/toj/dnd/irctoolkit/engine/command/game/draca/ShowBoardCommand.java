package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.PC;
import org.toj.dnd.irctoolkit.game.draca.Zone;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "showboard", args = {}, summary = ".showboard - 显示当前桌面的状况。显示的内容包括牌库剩余牌数，弃牌堆的内容，各个PC的手牌数和展示区的内容。")
public class ShowBoardCommand extends DracaGameCommand {

    public ShowBoardCommand(Object[] args) {
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException, ToolkitWarningException {
        for (PC pc : getGame().getPcs().values()) {
            StringBuilder sb = new StringBuilder(pc.getName());
            sb.append("手牌").append(getGame().getPcHand(pc.getName()).size()).append("张");
            if (!getGame().getPcs().get(pc.getName()).getZone(Zone.LOCKED).isEmpty()) {
                sb.append("，扣下的牌").append(getGame().getPcs().get(pc.getName()).getZone(Zone.LOCKED).size()).append("张");
            }
            if (!getGame().getPcDisplay(pc.getName()).isEmpty()) {
                sb.append("，展示区: ").append(getGame().getPcDisplay(pc.getName()).toText());
            }
            sendMsg(sb.toString());
        }
        sendMsg("牌库: " + getGame().getZone(Zone.DECK).getCards().size() + "张");
        sendMsg("弃牌堆: " + getGame().getZone(Zone.DISCARD).getCards().toString());
        return msgs;
    }
}

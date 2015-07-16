package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.game.draca.Zone;
import org.toj.dnd.irctoolkit.game.draca.PC;
import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

import java.util.List;

@IrcCommand(command = "resolve", args = {}, summary = ".resolve - DM专用。亮开所有玩家暗出的牌并移入弃牌堆。参见.fd")
public class ResolveCommand extends UndoableDracaGameCommand {

  public ResolveCommand(Object[] args) {}

  @Override
  public void doProcess() throws ToolkitCommandException, ToolkitWarningException {
    if (!isFromDm()) {
      sendMsg("只有DM可以结算暗出的牌。");
      return;
    }
    for (PC pc : getGame().getPcs().values()) {
      List<String> cards = pc.getZone(Zone.FACE_DOWN).getCards();
      if (cards.isEmpty()) {
        continue;
      }
      sendMsg(pc.getName() + "翻开了暗出的牌：" + StringUtils.join(cards, ", "));
      for (String card : cards) {
        getGame().move(card, getPcZoneName(pc.getName(), Zone.FACE_DOWN), Zone.DISCARD);
      }
    }
  }
}

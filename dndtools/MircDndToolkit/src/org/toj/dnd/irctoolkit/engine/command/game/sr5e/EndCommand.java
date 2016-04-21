package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "end", args = {}, summary = ".end - 结束当前角色的行动轮")
public class EndCommand extends Sr5eGameCommand {

    protected boolean updateTopic = true;

    public EndCommand(Object[] args) {
        super();
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        // skip if a battle or a round is not started
        if (getGame().isInBattle() || getGame().getBattle().getCurrent() == null) {
            return;
        }
        getGame().getBattle().endTurn();
        sendTopic(getGame().generateTopic());
        refreshTopic();
        if (getGame().getBattle().getCurrent() == null) { // hack. test if a new round has begun
            sendMsg("第" + getGame().getBattle().getRound() + "回合开始，请重投init。");
        } else {
            sendMsg("轮到" + getGame().getBattle().getCurrent().getName() + "行动了");
        }
    }
}

package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "end", args = {})
public class EndCommand extends D6smwGameCommand {

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
        getGame().end();
        sendTopic(getGame().generateTopic());
        refreshTopic();
        sendMsg("轮到" + getGame().getCurrent().getName() + "行动了");
    }
}

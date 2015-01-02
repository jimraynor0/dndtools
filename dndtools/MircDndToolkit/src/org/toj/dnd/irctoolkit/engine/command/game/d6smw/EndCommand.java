package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.d6smw.Mech;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

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
        getGame().getBattle().end();
        sendTopic(getGame().generateTopic());
        refreshTopic();
        sendMsg("轮到"
                + IrcColoringUtil.paint(getGame().getBattle().getCurrent()
                        .getName(), Color.PURPLE.getCode()) + "行动了");
        if (getGame().getBattle().getCurrent() instanceof Mech) {
            for (String line : getGame().generateStatString(
                    (Mech) getGame().getBattle().getCurrent())) {
                sendMsg(line);
            }
        }
    }
}

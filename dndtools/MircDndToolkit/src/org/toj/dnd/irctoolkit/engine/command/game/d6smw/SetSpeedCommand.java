package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.d6smw.Mech;

@IrcCommand(command = "speed", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_STRING, CommandSegment.STRING })
public class SetSpeedCommand extends D6smwGameCommand {

    private int speed;
    private String mech;
    private String direction;

    public SetSpeedCommand(Object[] args) {
        speed = (Integer) args[0];
        mech = (String) args[1];
        direction = (String) args[2];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        Mech m = null;
        if (StringUtils.isEmpty(mech)) {
            m = getGame().getMech(caller);
        } else {
            m = getGame().getMech(mech);
        }
        m.setSpeed(speed);
        m.setDirection(direction);
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

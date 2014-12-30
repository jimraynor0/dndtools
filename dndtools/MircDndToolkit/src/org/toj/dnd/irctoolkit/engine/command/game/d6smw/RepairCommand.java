package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "+", args = { CommandSegment.INT,
        CommandSegment.NULLABLE_STRING, CommandSegment.STRING })
public class RepairCommand extends D6smwGameCommand {

    private int value;
    private String mech;
    private String section;

    public RepairCommand(Object[] args) {
        value = (Integer) args[0];
        mech = (String) args[1];
        section = (String) args[2];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (StringUtils.isEmpty(mech)) {
            getGame().repair(caller, section, value);
        } else {
            getGame().repair(mech, section, value);
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

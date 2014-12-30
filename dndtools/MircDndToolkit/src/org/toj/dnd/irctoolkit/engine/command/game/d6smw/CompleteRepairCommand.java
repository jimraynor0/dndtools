package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.d6smw.D6smwGame;
import org.toj.dnd.irctoolkit.game.d6smw.Mech;

@IrcCommand(command = "fullrepair", args = { CommandSegment.NULLABLE_LIST })
public class CompleteRepairCommand extends D6smwGameCommand {

    private String[] mechs;

    public CompleteRepairCommand(Object[] args) {
        if (args.length > 0) {
            this.mechs = new String[args.length];
            System.arraycopy(args, 0, this.mechs, 0, args.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (mechs == null) {
            for (Mech mech : getGame().getMechs().values()) {
                mech.completeRepair();
            }
        } else {
            for (String name : mechs) {
                getGame().getMech(name).completeRepair();
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

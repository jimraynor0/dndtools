package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.d6smw.Mech;

@IrcCommand(command = "fire", args = { CommandSegment.LIST })
public class FireCommand extends D6smwGameCommand {

    private String name;
    private String[] equipments;

    public FireCommand(Object[] args) {
        if (getGame().getMech((String) args[0]) != null) {
            name = (String) args[0];
            this.equipments = new String[args.length - 1];
            System.arraycopy(args, 1, this.equipments, 0, equipments.length);
        } else {
            this.equipments = new String[args.length];
            System.arraycopy(args, 0, this.equipments, 0, equipments.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        Mech mech = null;
        if (StringUtils.isEmpty(name)) {
            mech = getGame().getMech(caller);
        } else {
            mech = getGame().getMech(name);
        }
        for (String eq : equipments) {
            String result = getGame().activateEquipment(mech, eq);
            if (!StringUtils.isEmpty(result)) {
                sendMsg(result);
            }
        }

        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

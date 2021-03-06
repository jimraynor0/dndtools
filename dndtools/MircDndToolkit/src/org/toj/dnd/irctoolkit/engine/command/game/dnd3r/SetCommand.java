package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

@IrcCommand(command = "set", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_STRING, CommandSegment.STRING })
public class SetCommand extends Dnd3rGameCommand {

    private String name;
    private String attr;
    private int value;

    public SetCommand(Object[] args) {
        this.value = (Integer) args[0];
        this.name = (String) args[1];
        this.attr = (String) args[2];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (name == null) {
            name = caller;
        }
        PC pc = getGame().findCharByNameOrAbbre(name);
        if (pc != null) {
            if ("xp".equalsIgnoreCase(attr)) {
                pc.setXp(value);
            }
            if ("hp".equalsIgnoreCase(attr)) {
                pc.setHp(value);
            }
            if ("maxHp".equalsIgnoreCase(attr)) {
                pc.setMaxHp(value);
            }
            if ("thp".equalsIgnoreCase(attr)) {
                pc.setThp(value);
            }
            if ("pp".equalsIgnoreCase(attr)) {
                pc.setPp(value);
            }
            if ("maxPp".equalsIgnoreCase(attr)) {
                pc.setMaxPp(value);
            }
            if ("initMod".equalsIgnoreCase(attr)) {
                pc.setInitMod(value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

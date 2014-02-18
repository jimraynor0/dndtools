package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;

@IrcCommand(command = "set", args = { CommandSegment.INT, CommandSegment.NULLABLE_STRING, CommandSegment.STRING })
public class SetCommand extends UndoableTopicCommand {

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
            if ("surge".equalsIgnoreCase(attr)) {
                pc.setSurge(value);
            }
            if ("maxSurge".equalsIgnoreCase(attr)) {
                pc.setMaxSurge(value);
            }
            if ("ap".equalsIgnoreCase(attr)) {
                pc.setAp(value);
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
            if ("psionic".equalsIgnoreCase(attr)) {
                pc.setPsionic(1 == value);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

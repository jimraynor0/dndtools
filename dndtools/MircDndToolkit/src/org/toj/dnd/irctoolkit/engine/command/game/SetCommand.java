package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;

public class SetCommand extends UndoableTopicCommand {

    private String name;
    private String attr;
    private int value;

    public SetCommand(String attr, int value) {
        this.attr = attr;
        this.value = value;
    }

    public SetCommand(String attr, int value, String name) {
        this.attr = attr;
        this.value = value;
        this.name = name;
    }

    public SetCommand(String attr, String name, int value) {
        this.attr = attr;
        this.value = value;
        this.name = name;
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

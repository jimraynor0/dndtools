package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;

public class ModifyXpCommand extends UndoableTopicCommand {

    private String[] pcs;
    private int value;

    public ModifyXpCommand(int value, String[] pcs) {
        this.value = value;
        this.pcs = pcs;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (pcs != null && pcs.length > 0) {
            for (String pcName : pcs) {
                PC pc = getGame().findCharByNameOrAbbre(pcName);
                if (pc != null) {
                    pc.setXp(pc.getXp() + value);
                }
            }
        } else {
            for (PC pc : getGame().getPcs().values()) {
                pc.setXp(pc.getXp() + value);
            }
        }
    }
}

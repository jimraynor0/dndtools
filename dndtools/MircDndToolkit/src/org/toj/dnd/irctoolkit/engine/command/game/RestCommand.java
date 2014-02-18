package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;

// temporarily skipped
public class RestCommand extends UndoableTopicCommand {
    private String[] chars;
    private String type;

    public RestCommand(String type, String[] chars) {
        this.type = type;
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.chars.length == 0) {
            if (type.equals("shortrest")) {
                getGame().applyShortRest();
            }
            if (type.equals("extendedrest")) {
                getGame().applyExtendedRest();
            }
            if (type.equals("milestone")) {
                getGame().applyMilestone();
            }
        } else {
            for (String ch : chars) {
                PC pc = getGame().findCharByNameOrAbbre(ch);
                if (pc != null && type.equals("shortrest")) {
                    pc.applyShortRest();
                }
                if (pc != null && type.equals("extendedrest")) {
                    pc.applyExtendedRest();
                }
                if (pc != null && type.equals("milestone")) {
                    pc.applyMilestone();
                }
            }
        }
        if (type.equals("extendedrest")) {
            sendTopic(getGame().generateTopic());
            refreshTopic();
        }
    }
}

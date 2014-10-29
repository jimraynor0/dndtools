package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "nonlethal", args = { CommandSegment.INT,
        CommandSegment.NULLABLE_LIST })
public class NonlethalDamageCommand extends UndoableTopicCommand {

    private int value;
    private String[] chars;

    public NonlethalDamageCommand(Object[] args) {
        value = (Integer) args[0];
        if (args.length == 1) {
            chars = new String[0];
        } else {
            chars = new String[args.length - 1];
            System.arraycopy(args, 1, chars, 0, chars.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            dealNonLethalDmg(caller);
        } else {
            for (String charName : chars) {
                dealNonLethalDmg(charName);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }

    private void dealNonLethalDmg(String charName) {
        if (getGame().inBattle()) {
            getGame().getBattle().findCharByNameOrAbbre(charName)
                    .nonlethalDamage(value);
        } else {
            getGame().findCharByNameOrAbbre(charName).nonlethalDamage(value);
        }
    }
}

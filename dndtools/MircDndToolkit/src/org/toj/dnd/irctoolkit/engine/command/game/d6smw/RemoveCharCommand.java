package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "kill", args = { CommandSegment.LIST })
public class RemoveCharCommand extends D6smwGameCommand {

    private String[] chars;

    public RemoveCharCommand(Object[] args) {
        this.chars = new String[args.length];
        System.arraycopy(args, 0, this.chars, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        for (String charName : chars) {
            if (getGame().getMech(charName) != null) {
                getGame().removeMechFromBattle(getGame().getMech(charName));
            }
        }
        sendTopic(getGame().generateTopic());
        refreshTopic();
    }
}

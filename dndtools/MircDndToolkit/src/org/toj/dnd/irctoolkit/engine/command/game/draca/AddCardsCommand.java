package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "addcard", args = { CommandSegment.INT, CommandSegment.STRING, CommandSegment.NULLABLE_LIST })
public class AddCardsCommand extends UndoableDracaGameCommand {

    private int amount;
    private String name;
    private String text;

    public AddCardsCommand(Object[] args) {
        amount = (Integer) args[0];
        name = (String) args[1];

        if (args.length > 2) {
            String[] textParts = new String[args.length - 2];
            System.arraycopy(args, 2, textParts, 0, textParts.length);
            text = composite(textParts);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().addCards(name, text, amount);
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

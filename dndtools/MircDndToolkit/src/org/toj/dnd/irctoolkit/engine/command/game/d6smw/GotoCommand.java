package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "goto", args = { CommandSegment.NULLABLE_INT,
        CommandSegment.STRING })
public class GotoCommand extends D6smwGameCommand {

    private String charName;
    private int round = Integer.MIN_VALUE;

    public GotoCommand(Object[] args) {
        this.round = args[0] == null ? Integer.MIN_VALUE : (Integer) args[0];
        this.charName = (String) args[1];
    }

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.round == Integer.MIN_VALUE) {
            getGame().go(getGame().getMech(charName), getGame().getRound());
        } else {
            getGame().go(getGame().getMech(charName), round);
        }
        sendTopic(getGame().generateTopic());
        refreshTopic();
        sendMsg("轮到" + getGame().getCurrent().getName() + "行动了");
    }
}

package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.Zone;

@IrcCommand(command = "hide", args = { CommandSegment.STRING })
public class HideDisplayCardCommand extends DracaGameCommand {

    private String card;

    public HideDisplayCardCommand(Object[] args) {
        card = (String) args[0];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        getGame().move(card, getPcZoneName(caller, Zone.DISPLAY), getPcZoneName(caller, Zone.HAND));
        sendMsg(caller + "从展示区拿回了" + card);
    }
}

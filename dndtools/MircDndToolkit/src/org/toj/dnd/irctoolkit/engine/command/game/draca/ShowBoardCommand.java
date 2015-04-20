package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.draca.PC;

@IrcCommand(command = "showboard", args = { CommandSegment.NULLABLE_STRING })
public class ShowBoardCommand extends DracaGameCommand {

    // target can be either pc or discard. default to pc
    private String target = "pc";

    public ShowBoardCommand(Object[] args) {
        if (args.length > 0) {
            target = (String) args[0];
            if (!"pc".equalsIgnoreCase(target) && !"discard".equalsIgnoreCase(target)) {
                target = "pc";
            }
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if ("pc".equalsIgnoreCase(target)) {
            for (PC pc : getGame().getPcs().values())
            sendMsg(pc.getName() + ": " + getGame().getPcDisplay(pc.getName()).getCards());
        } else {
            sendMsg(getGame().getZone(target).getCards().toString());
        }
    }
}

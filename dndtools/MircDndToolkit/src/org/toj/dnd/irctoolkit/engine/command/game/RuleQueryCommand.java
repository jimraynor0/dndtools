package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.rules.RuleBook;

@IrcCommand(command="rule", args = {CommandSegment.LIST})
public class RuleQueryCommand extends GameCommand {

    private String elements;

    public RuleQueryCommand(Object[] args) {
        elements = composite(args);
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        List<String> result = RuleBook.getRuleBook().query(elements.toString());
        if (result != null && !result.isEmpty()) {
            for (String line : result) {
                this.sendMsg(line.trim());
            }
        } else {
            this.sendMsg("cannot find rule: [" + elements + "]");
        }
        return this.msgs;
    }

    public boolean requireGameContext() {
        return false;
    }
}

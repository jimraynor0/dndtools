package org.toj.dnd.irctoolkit.engine.command;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;

public class IrcCommandPatternInterpreter {
    private String command;
    private List<CommandSegment> args;

    public IrcCommandPatternInterpreter(IrcCommand anno) {
        this.command = anno.command();
        this.args = Arrays.asList(anno.args());
    }

    public boolean matches(String[] parts) {
        if (!command.equals(parts[0])) {
            return false;
        }
        for (int i = 0; i < Math.min(args.size(), parts.length); i++) {
            
        }
        return true;
    }
}

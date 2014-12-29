package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.log.GameLogManager;

@IrcCommand(command = "log", args = { CommandSegment.LIST })
public class LogCommand extends UndoableTopicCommand {

    private String[] args;

    public LogCommand(Object[] args) {
        this.args = new String[args.length];
        System.arraycopy(args, 0, this.args, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (args.length == 0) {
            return;
        } else {
            if (args[0].equals("del") || args[0].equals("delete")) {
                GameLogManager.getInstance().removeLog(
                        composite(Arrays.copyOfRange(args, 1, args.length)));
            } else if (args[0].equals("query")) {
                List<String> results = GameLogManager.getInstance().query(
                        composite(Arrays.copyOfRange(args, 1, args.length)));
                for (String line : results) {
                    sendMsg(line);
                }
            } else {
                String[] parts = composite(args).split("\\|");
                GameLogManager.getInstance().addLog(parts[0], parts[1], caller);
            }
        }
    }
}

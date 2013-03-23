package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.log.GameLogManager;

public class LogCommand extends UndoableTopicCommand {

    private String[] args;

    public LogCommand(String[] args) {
        this.args = args;
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

    private String composite(String[] parts) {
        StringBuilder sb = new StringBuilder();
        for (String str : parts) {
            if (!str.equals(parts[0])) {
                sb.append(" ");
            }
            sb.append(str);
        }
        return sb.toString();
    }
}

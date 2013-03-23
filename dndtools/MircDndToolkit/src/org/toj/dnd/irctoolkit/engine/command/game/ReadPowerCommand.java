package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class ReadPowerCommand extends GameCommand {
    private String[] args;

    public ReadPowerCommand(String[] args) {
        this.args = args;
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        StringBuilder sb = new StringBuilder();
        for (String e : args) {
            if (e != args[0]) {
                sb.append(" ");
            }
            sb.append(e);
        }
        List<String> powerRead = getGame().readPower(sb.toString());
        if (powerRead != null) {
            for (String line : powerRead) {
                this.sendMsg(line.trim());
            }
        }
        return this.msgs;
    }

    public boolean requireGameContext() {
        return false;
    }
}

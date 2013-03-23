package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;
import org.toj.dnd.irctoolkit.game.Power;

public class PowerGroupCommand extends UndoableTopicCommand {

    private PC pc;
    private String group;
    private List<String> powers;

    public PowerGroupCommand(String[] args) {
        String first = args[0];
        String cmd;
        if (getGame().findCharByNameOrAbbre(first) != null) {
            pc = getGame().findCharByNameOrAbbre(first);
            cmd = StringUtils.join(args, ' ', 1, args.length);
        } else {
            cmd = StringUtils.join(args, ' ');
        }

        String[] params = cmd.split("\\=");
        group = params[0].trim();
        powers = Arrays.asList(params[1].split("\\|"));
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (pc == null) {
            pc = getGame().findCharByNameOrAbbre(caller);
        }
        for (Power power : pc.getPowersInGroup(group)) {
            power.setGroup(null);
        }
        for (String power : powers) {
            Power p = pc.findPower(power.trim());
            if (p != null) {
                p.setGroup(group);
            }
        }
    }
}

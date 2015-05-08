package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;
import org.toj.dnd.irctoolkit.game.dnd3r.Spell;

@IrcCommand(command = "cast", args = { CommandSegment.LIST }, summary = ".cast 法术 - 施展法术，这会将该法术从你的法术列表中移除。")
public class CastSpellCommand extends Dnd3rGameCommand {

    private PC pc;
    private String spell;

    public CastSpellCommand(Object[] args) {
        pc = getGame().findCharByNameOrAbbre((String) args[0]);
        List<Object> argList = Arrays.asList(args);
        if (pc != null) {
            argList = argList.subList(1, argList.size());
        }
        spell = composite(argList.toArray());
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (pc == null) {
            pc = getGame().findCharByNameOrAbbre(caller);
        }
        String result = pc.removeSpell(new Spell(spell, 1));
        if (result != null) {
            super.sendMsg(result);
        }
    }
}

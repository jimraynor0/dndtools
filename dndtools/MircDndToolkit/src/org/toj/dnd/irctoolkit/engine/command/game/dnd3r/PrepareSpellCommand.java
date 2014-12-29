package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;
import org.toj.dnd.irctoolkit.game.dnd3r.Spell;

@IrcCommand(command = "prepare", args = { CommandSegment.LIST })
public class PrepareSpellCommand extends Dnd3rGameCommand {
    private PC pc;
    private String group;
    private List<Spell> spells;

    public PrepareSpellCommand(Object[] args) {
        pc = getGame().findCharByNameOrAbbre((String) args[0]);
        List<Object> argList = Arrays.asList(args);
        if (pc != null) {
            argList = argList.subList(1, argList.size());
        }
        group = (String) argList.get(0);
        argList = argList.subList(1, argList.size());

        spells = new ArrayList<Spell>();
        for (String spellStr : composite(argList.toArray()).split("\\|")) {
            if (spellStr.contains("*")) {
                String[] spellParts = spellStr.split("\\*");
                spells.add(new Spell(spellParts[0], Integer
                    .parseInt(spellParts[1])));
            } else {
                spells.add(new Spell(spellStr.trim(), 1));
            }
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (pc == null) {
            pc = getGame().findCharByNameOrAbbre(caller);
        }
        for (Spell spell : spells) {
            pc.addSpell(group, spell);
        }
    }
}

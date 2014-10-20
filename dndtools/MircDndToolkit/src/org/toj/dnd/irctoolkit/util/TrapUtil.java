package org.toj.dnd.irctoolkit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.toj.common.FileIoUtils;
import org.toj.dnd.irctoolkit.dice.Dice;
import org.toj.dnd.irctoolkit.rules.Rule;
import org.toj.dnd.irctoolkit.rules.RuleBook;

public class TrapUtil {
    private static final File TRAPS_FILE = new File("./config/traps.config");
    private static Map<Integer, List<Trap>> ALL_TRAPS;

    public static Trap getRandomTrap(int cr) throws IOException {
        List<Trap> traps = getTrapList(cr);
        Trap trap = traps.get(Dice.getDice(traps.size()).roll() - 1);
        return trap;
    }

    public static List<Trap> getTrapList(int cr) throws IOException {
        return loadTrapListFromFile().get(cr);
    }

    private static Map<Integer, List<Trap>> loadTrapListFromFile()
        throws IOException {
        if (ALL_TRAPS == null) {
            ALL_TRAPS = new HashMap<Integer, List<Trap>>();
            BufferedReader reader = FileIoUtils.getReader(TRAPS_FILE);
            String line = null;

            String name = null;
            String desc = null;
            while ((line = reader.readLine()) != null) {
                if (line == null || line.isEmpty()) {
                    if (name == null || desc == null) {
                        continue;
                    }

                    Trap trap = new Trap();
                    trap.name = name;
                    trap.desc = desc;

                    String crStr = desc.split(";")[0];
                    int cr = Integer.parseInt(crStr.substring(3).trim());
                    
                    if (!ALL_TRAPS.containsKey(cr)) {
                        ALL_TRAPS.put(cr, new LinkedList<Trap>());
                    }
                    ALL_TRAPS.get(cr).add(trap);

                    addToRule(trap);

                    name = null;
                    desc = null;
                } else if (name == null) {
                    name = line;
                } else if (desc == null) {
                    desc = line;
                }
            }
            reader.close();
        }
        return ALL_TRAPS;
    }

    private static void addToRule(Trap trap) {
        Rule rule = new Rule();
        rule.setName(trap.name);
        rule.setType("Trap");
        rule.setText(trap.desc);
        RuleBook.getRuleBook().addToRuleSet(rule);
    }

    public static class Trap {
        public String name;
        public String desc;
    }
}

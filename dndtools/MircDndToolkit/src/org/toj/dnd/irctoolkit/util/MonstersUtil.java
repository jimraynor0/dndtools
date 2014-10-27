package org.toj.dnd.irctoolkit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.toj.common.FileIoUtils;
import org.toj.dnd.irctoolkit.dice.Dice;

public class MonstersUtil {
    private static final File MM1_CR_FILE = new File(
            "./resources/monsters/mm1cr.txt");
    private static final File MM1_PAGES_FILE = new File(
            "./resources/monsters/mm1pages.txt");
    private static Map<String, List<String>> MONSTERS;

    public static String getRandomMonsterByCr(String cr) throws IOException {
        List<String> traps = getMonsterList(cr);
        String trap = traps.get(Dice.getDice(traps.size()).roll() - 1);
        return trap;
    }

    public static List<String> getMonsterList(String cr) throws IOException {
        return loadFromFile().get(cr);
    }

    private static Map<String, List<String>> loadFromFile() throws IOException {
        if (MONSTERS == null) {
            MONSTERS = new HashMap<String, List<String>>();

            BufferedReader reader = FileIoUtils.getReader(MM1_PAGES_FILE);
            String line = null;
            Map<String, String> monstersByPage = new HashMap<String, String>();

            while ((line = reader.readLine()) != null) {
                if (!line.contains(".")) {
                    line += reader.readLine();
                }
                List<String> splited = Arrays.asList(line.split("\\."));
                monstersByPage.put(splited.get(0).trim(), splited.get(0).trim()
                        + " - page " + splited.get(splited.size() - 1).trim());
            }
            reader.close();

            reader = FileIoUtils.getReader(MM1_CR_FILE);
            line = null;
            Map<String, String> monstersByCr = new HashMap<String, String>();

            while ((line = reader.readLine()) != null) {
                if (!line.contains(".")) {
                    line += reader.readLine();
                }
                List<String> splited = Arrays.asList(line.split("\\."));
                monstersByCr.put(splited.get(0).trim(),
                        splited.get(splited.size() - 1).trim());
            }
            reader.close();

            Set<String> removes = new HashSet<String>(monstersByCr.size());
            for (String name : monstersByCr.keySet()) {
                putIntoMonsters(name, monstersByCr.get(name), monstersByPage,
                        removes);
            }
            for (String key : removes) {
                monstersByPage.remove(key);
            }
            for (String name : monstersByPage.keySet()) {
                System.out.println(name + " is not mapped to a cr.");
            }
        }

        return MONSTERS;
    }

    private static void putIntoMonsters(String name, String cr,
            Map<String, String> monstersByPage, Set<String> removes) {
        String base = name.split(",")[0];
        if (base.contains("(")) {
            base = base.substring(0, base.indexOf("(")).trim();
        }

        for (String key : monstersByPage.keySet()) {
            if (key.contains(base)) {
                if (!MONSTERS.containsKey(cr)) {
                    MONSTERS.put(cr, new ArrayList<String>());
                }
                MONSTERS.get(cr).add(monstersByPage.get(key));
                removes.add(key);
                return;
            }
        }

        System.out.println(name + " is not found in page index");
    }

    public static void main(String[] args) throws IOException {
        loadFromFile();
        int size = 0;
        for (List<String> list : MONSTERS.values()) {
            size += list.size();
        }
        System.out.println("size: " + size);
    }
}

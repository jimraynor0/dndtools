package org.toj.dnd.irctoolkit.engine.command;

import java.util.Arrays;
import java.util.LinkedList;
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
        int intArgsMin = 0;
        int intArgsMax = 0;
        int strArgsMin = 0;
        int strArgsMax = 0;
        for (CommandSegment cs : args) {
            if ("int".equals(cs.type())) {
                if (!cs.isNullable()) {
                    intArgsMin++;
                }
                intArgsMax++;
            }
            if ("string".equals(cs.type())) {
                if (!cs.isNullable()) {
                    strArgsMin++;
                }
                strArgsMax++;
            }
            if ("list".equals(cs.type())) {
                if (!cs.isNullable()) {
                    strArgsMin++;
                }
                strArgsMax = Integer.MAX_VALUE;
            }
        }
        return findIntArgs(parts).size() >= intArgsMin
            && findIntArgs(parts).size() <= intArgsMax
            && findStrArgs(parts).size() >= strArgsMin
            && findStrArgs(parts).size() >= strArgsMax;
    }

    public List<Object> sortArgs(String[] actualArgs) {
        List<Object> result = new LinkedList<Object>();
        if (actualArgs == null || actualArgs.length == 0) {
            return result;
        }

        int intArgsMin = 0;
        int strArgsMin = 0;
        for (CommandSegment cs : args) {
            if ("int".equals(cs.type())) {
                if (!cs.isNullable()) {
                    intArgsMin++;
                }
            }
        }

        List<Integer> intArgs = findIntArgs(actualArgs);
        result.addAll(intArgs);
        if (intArgs.size() < intArgsMin) {
            for (int i = 0; i < intArgsMin - intArgs.size(); i++) {
                result.add(null);
            }
        }
        List<String> strArgs = findStrArgs(actualArgs);
        result.addAll(strArgs);
        return result;
    }

    private List<Integer> findIntArgs(String[] parts) {
        List<Integer> result = new LinkedList<Integer>();
        if (parts == null || parts.length == 0) {
            return result;
        }

        for (String p : parts) {
            if (p == parts[0] && command.equals(p)) {
                continue;
            }
            if (isInteger(p)) {
                result.add(Integer.parseInt(p));
            }
        }
        return result;
    }

    private List<String> findStrArgs(String[] parts) {
        List<String> result = new LinkedList<String>();
        if (parts == null || parts.length == 0) {
            return result;
        }

        for (String p : parts) {
            if (p == parts[0] && command.equals(p)) {
                continue;
            }
            if (!isInteger(p)) {
                result.add(p);
            }
        }
        return result;
    }

    private boolean isInteger(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

package org.toj.dnd.irctoolkit.engine.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.util.StringNumberUtil;

public class IrcCommandPatternInterpreter {
    private String command;
    private List<CommandSegment> args;
    private int intArgsMin = 0;
    private int intArgsMax = 0;
    private int doubleArgsMin = 0;
    private int doubleArgsMax = 0;
    private int strArgsMin = 0;
    private int strArgsMax = 0;
    private boolean hasList;

    public IrcCommandPatternInterpreter(IrcCommand anno) {
        this.command = anno.command();
        this.args = Arrays.asList(anno.args());
        
        for (CommandSegment cs : args) {
            if ("int".equals(cs.type())) {
                intArgsMax++;
                if (!cs.isNullable()) {
                    intArgsMin++;
                }
            }
            if ("double".equals(cs.type())) {
                doubleArgsMax++;
                if (!cs.isNullable()) {
                    doubleArgsMin++;
                }
            }
            if ("str".equals(cs.type())) {
                strArgsMax++;
                if (!cs.isNullable()) {
                    strArgsMin++;
                }
            }
            if ("list".equals(cs.type())) {
                hasList = true;
            }
        }

    }

    public boolean matches(String[] parts) {
        return command.equalsIgnoreCase(parts[0]);
        /*
         * if (!command.equals(parts[0])) { return false; } int intArgsMin = 0;
         * int intArgsMax = 0; int strArgsMin = 0; int strArgsMax = 0; for
         * (CommandSegment cs : args) { if ("int".equals(cs.type())) { if
         * (!cs.isNullable()) { intArgsMin++; } intArgsMax++; } if
         * ("string".equals(cs.type())) { if (!cs.isNullable()) { strArgsMin++;
         * } strArgsMax++; } if ("list".equals(cs.type())) { if
         * (!cs.isNullable()) { strArgsMin++; } strArgsMax = Integer.MAX_VALUE;
         * } } return findIntArgs(parts).size() >= intArgsMin &&
         * findIntArgs(parts).size() <= intArgsMax && findStrArgs(parts).size()
         * >= strArgsMin && findStrArgs(parts).size() >= strArgsMax;
         */}

    public Object[] sortArgs(String[] originalArgs) {
        List<Object> result = new LinkedList<Object>();
        if (originalArgs == null || originalArgs.length == 0) {
            return new Object[0];
        }

        List<String> argsList = new LinkedList<String>();
        argsList.addAll(Arrays.asList(originalArgs));

        if (doubleArgsMax > 0) {
            List<Double> doubleArgs = findDoubleArgs(argsList);
            if (doubleArgs.size() < intArgsMax) {
                for (int i = 0; i < intArgsMax - doubleArgs.size(); i++) {
                    result.add(null);
                }
            }
            result.addAll(doubleArgs);
        }

        if (intArgsMax > 0) {
            List<Integer> intArgs = findIntArgs(argsList);
            if (intArgs.size() < intArgsMax) {
                for (int i = 0; i < intArgsMax - intArgs.size(); i++) {
                    result.add(null);
                }
            }
            result.addAll(intArgs);
        }

        if (!hasList && argsList.size() < strArgsMax) {
            for (int i = 0; i < strArgsMax - argsList.size(); i++) {
                result.add(null);
            }
        }
        result.addAll(argsList);
        return result.toArray(new Object[result.size()]);
    }

    private List<Double> findDoubleArgs(List<String> parts) {
        List<Double> result = new LinkedList<Double>();
        if (parts == null || parts.size() == 0) {
            return result;
        }

        Iterator<String> i = parts.iterator();
        while (i.hasNext()) {
            String p = i.next();
            if (StringNumberUtil.isDouble(p)) {
                result.add(Double.parseDouble(p));
                i.remove();
            }
        }
        return result;
    }

    private List<Integer> findIntArgs(List<String> parts) {
        List<Integer> result = new LinkedList<Integer>();
        if (parts == null || parts.size() == 0) {
            return result;
        }

        Iterator<String> i = parts.iterator();
        while (i.hasNext()) {
            String p = i.next();
            if (StringNumberUtil.isInteger(p)) {
                result.add(Integer.parseInt(p));
                i.remove();
            }
        }
        return result;
    }
}

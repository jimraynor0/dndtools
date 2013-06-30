package org.toj.dnd.irctoolkit.engine.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IrcCommandPatternInterpreter {
    private int argsMin;
    private int argsMax;
    private List<Pattern> patterns;

    public IrcCommandPatternInterpreter(IrcCommand anno) {
        this.argsMin = anno.argsMin();
        this.argsMax = anno.argsMax();
        this.patterns = new ArrayList<Pattern>(anno.patterns().length);
        for (String p : anno.patterns()) {
            patterns.add(Pattern.compile(p));
        }
    }

    public boolean matches(String[] parts) {
        if (parts.length > argsMax || parts.length < argsMin) {
            return false;
        }
        for (int i = 0; i < Math.min(patterns.size(), parts.length); i++) {
            if (!patterns.get(i).matcher(parts[i]).matches()) {
                return false;
            }
        }
        return true;
    }
}

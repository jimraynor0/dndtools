package org.toj.dnd.irctoolkit.dice;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DiceRoll {
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            Object result = engine.eval("3+4");
            System.out.println(result);
            System.out.println(result.getClass());
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}

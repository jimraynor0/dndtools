package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

public class Mech {
    private String name;
    private String model;
    private int heat;
    private int heatSink;
    private Map<String, Section> sections = new HashMap<String, Section>();
    private Map<String, Equipment> equipments =
        new HashMap<String, Equipment>();

    public Mech(Element mechElement) {
        // TODO Auto-generated constructor stub
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
}

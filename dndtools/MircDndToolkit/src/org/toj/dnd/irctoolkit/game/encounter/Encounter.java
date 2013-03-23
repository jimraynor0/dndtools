package org.toj.dnd.irctoolkit.game.encounter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public class Encounter {

    public String name;
    public String mapName;
    public Map<String, NpcTemplate> npcs;
    public List<String> notes;

    public Encounter(String name) {
        super();
        this.name = name;
        npcs = new HashMap<String, NpcTemplate>();
        notes = new LinkedList<String>();
    }

    public Encounter(Element e) {
        this(e.elementTextTrim("name"));
        if (e.element("mapName") != null) {
            mapName = e.elementTextTrim("mapName");
        }
        if (e.element("npcs") != null) {
            Iterator<Element> i = e.element("npcs").elementIterator();
            while (i.hasNext()) {
                Element eNpc = i.next();
                NpcTemplate npc = new NpcTemplate(eNpc);
                this.npcs.put(npc.getName(), npc);
            }
        }
        if (e.element("notes") != null) {
            Iterator<Element> i = e.element("notes").elementIterator();
            while (i.hasNext()) {
                Element eNote = i.next();
                this.notes.add(eNote.getTextTrim());
            }
        }
    }
}

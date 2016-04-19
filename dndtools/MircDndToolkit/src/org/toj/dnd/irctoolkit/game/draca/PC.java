package org.toj.dnd.irctoolkit.game.draca;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class PC {
    private String name;
    private Map<String, Zone> pcZones;

    public PC() {
        pcZones = new HashMap<>();
    }

    public PC(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Zone getZone(String zone) {
        if (!pcZones.containsKey(zone)) {
            pcZones.put(zone, new Zone());
        }
        return this.pcZones.get(zone);
    }

    public Map<String, Zone> getPcZones() {
        return pcZones;
    }
}

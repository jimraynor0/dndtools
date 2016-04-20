package org.toj.dnd.irctoolkit.game.sr5e;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Sr5eGame {
    private Map<String, PC> mechs = new HashMap<String, PC>();
    private Battle battle;

}

package org.toj.dnd.irctoolkit.game;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Game {

    private String dm;
    private String name;
    private Map<String, String> aliases = new HashMap<String, String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    public String mapAlias(String alias) {
        return aliases.get(alias);
    }

    public boolean hasAliases(String alias) {
        return aliases.containsKey(alias);
    }

    public void addAlias(String alias, String phrase) {
        // ensure that: only 1 alias is mapped to any given phrase
        // if alias itself is the value of an alias, then find the original
        // alias
        for (String key : aliases.keySet()) {
            if (alias.equals(aliases.get(key))) {
                alias = key;
            }
        }
        this.aliases.put(alias, phrase);
    }

    public void removeAlias(String alias) {
        this.aliases.remove(alias);
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public boolean isDm(String caller) {
        return dm == null || dm.isEmpty() || dm.equals(caller);
    }

    public abstract String getRuleSet();

    public abstract String generateTopic();

    public abstract String getGameCommandPackage();
}

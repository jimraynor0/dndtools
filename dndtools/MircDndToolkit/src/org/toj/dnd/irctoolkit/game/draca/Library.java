package org.toj.dnd.irctoolkit.game.draca;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Library {
    private Map<String, Card> lib;

    public Library() {
        lib = new HashMap<>();
    }

    public void add(Card c) {
        lib.put(c.getName(), c);
    }

    public int size() {
        return lib.size();
    }

    public boolean isEmpty() {
        return lib.isEmpty();
    }

    public boolean containsKey(Object key) {
        return lib.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return lib.containsValue(value);
    }

    public Card get(Object key) {
        return lib.get(key);
    }

    public Card put(String key, Card value) {
        return lib.put(key, value);
    }

    public Card remove(Object key) {
        return lib.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Card> m) {
        lib.putAll(m);
    }

    public void clear() {
        lib.clear();
    }

    public Set<String> keySet() {
        return lib.keySet();
    }

    public Collection<Card> values() {
        return lib.values();
    }

    public Set<Entry<String, Card>> entrySet() {
        return lib.entrySet();
    }

}

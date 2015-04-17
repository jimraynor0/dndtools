package org.toj.dnd.irctoolkit.game.draca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Zone implements Iterable<String> {
    // PC zones
    public static final String HAND = "hand";
    public static final String DISPLAY = "display";

    // public zones
    public static final String DECK = "deck";
    public static final String DISCARD = "discard";

    private List<String> cards = new ArrayList<>();

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean contains(Object o) {
        return cards.contains(o);
    }

    public Iterator<String> iterator() {
        return cards.iterator();
    }

    public boolean add(String e) {
        return cards.add(e);
    }

    public boolean remove(Object o) {
        return cards.remove(o);
    }

    public boolean addAll(Collection<? extends String> c) {
        return cards.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends String> c) {
        return cards.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return cards.removeAll(c);
    }

    public void clear() {
        cards.clear();
    }

    public String get(int index) {
        return cards.get(index);
    }

    public String set(int index, String element) {
        return cards.set(index, element);
    }

    public void add(int index, String element) {
        cards.add(index, element);
    }

    public String remove(int index) {
        return cards.remove(index);
    }

    public int indexOf(Object o) {
        return cards.indexOf(o);
    }

    public List<String> subList(int fromIndex, int toIndex) {
        return cards.subList(fromIndex, toIndex);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}

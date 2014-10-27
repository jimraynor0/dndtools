package org.toj.dnd.irctoolkit.token;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ModelList implements Iterable<Model>, Serializable {
    private static final long serialVersionUID = 3164050891258715638L;

    private List<Model> list = new LinkedList<Model>();

    public void add(int index, Model element) {
        list.add(index, element);
    }

    public boolean add(Model e) {
        return list.add(e);
    }

    public boolean addAll(Collection<? extends Model> c) {
        return list.addAll(c);
    }

    public void clear() {
        list.clear();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    public Model get(int index) {
        return list.get(index);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<Model> iterator() {
        return list.iterator();
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public Model remove(int index) {
        return list.remove(index);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public Model set(int index, Model element) {
        return list.set(index, element);
    }

    public int size() {
        return list.size();
    }
}

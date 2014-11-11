package org.sheepy.data.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Iterator wrapper for an Enumeration object. 
 * @param <T> Type of items
 */
public class EnumIterator<T> implements Iterator<T> {
  /** The real Enumeration object. */
  private Enumeration<T> e;

  /** Create a new wrapper for given Enumeration.
   *
   * @param E The Enumeration object to wrap up.
   * @throws IllegalArgumentException if the parameter is null.
   **/
  public EnumIterator(Enumeration<T> E) {
    if (E == null) new IllegalArgumentException();
    e = E;
  }

  /**
   * Return the Enumeration backing up this object.
   * @return backup Enumeration object
   */
  public Enumeration<T> getEnumeration() { return e; }

  public boolean hasNext() { return e.hasMoreElements();}
  public T next() { return e.nextElement(); }
  /** This methos always throws UnsupportedOperationException since
   *  Enumeration does not support removal of items.
   **/
  public void remove() {
    new UnsupportedOperationException("Enumeration does not support removal of item.");
  }
}
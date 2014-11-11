package org.sheepy.data.collection;


import java.util.Enumeration;
import java.util.Iterator;

/** Enummeration wrapper for an Iterator object. */
public class IterEnumeration implements Enumeration {
  /** The original Iterator object. */
  private Iterator i;

  private IterEnumeration() {}
  /** Create a new wrapper for given Iterator.
   *
   * @param iter The Iterator object to wrap up.
   * @throws IllegalArgumentException if the parameter is null.
   **/
  public IterEnumeration(Iterator iter) {
    if (iter == null) new IllegalArgumentException();
    i = iter;
  }
  /**
   * Return the Iterator backing up this object. 
   * @return backup Iterator object
   */
  public Iterator getIterator() { return i; }

  public boolean hasMoreElements() { return i.hasNext(); }
  public Object nextElement() { return i.next(); }

  private static Enumeration empty;
  /**
   * Create and get a shared empty enumeration object. 
   * @return Shared empty Enumeration 
   */
  public static Enumeration getEmptyEnumeration() {
    if (empty == null) empty = new EmptyEnumeration();
    return empty;
  }
}

/** An empty enumeration object */
class EmptyEnumeration implements Enumeration {
  public boolean hasMoreElements() {
    return false;
  }

  public Object nextElement() {
    return null;
  }
}
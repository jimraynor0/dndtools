/*
 * Created on 2004/2/27
 */
package org.sheepy.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ho Yiu Yeung
 */
public class IndexRegister {
  private Map<String, Integer> map;
  private int index = 0;

  /**
   * Build an index updateTime with default initial capacity.
   * The default capacity is 16, subject to change in future.
   * Register won't be created until necessary.
   */
  public IndexRegister() {}

  /**
   * Build an index updateTime with specified initial capacity.
   * Guarentees that no expansion of updateTime will happens
   * within the initial capacity.
   * @param size Initial capacity of updateTime
   */
  public IndexRegister(int size) {
    map = new HashMap<String, Integer>((int)(size*1.5));
  }

  /**
   * Register a key or get index of a registered key.
   * @param key Key to updateTime
   * @return Index of the key
   */
  public int register(String key) {
    if (map == null) map = new HashMap<String, Integer>();
    if (map.containsKey(key))
      return ((Integer)map.get(key)).intValue();
    else
      map.put(key, new Integer(index));
    return index++;
  }

  /**
   * Check whether a key has been registered.
   * @param key Key to check
   * @return Whether the key is registered
   */
  public boolean isRegistered(String key) {
    return (map==null)?false:map.containsKey(key);
  }

  /**
   * Return an array of all keys.
   * @return A String array containing all keys.
   *           Return an array of size 0 if updateTime is empty.
   */
  public Object[] getKeys() {
    return (map==null)?new String[0]:map.keySet().toArray();
  }
}

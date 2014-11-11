package org.sheepy.data;

import org.sheepy.util.DataUtil;

/**
 * An auto-expanding long integer array
 * Created by Ho Yiu Yeung on 04-Apr-2005 at 19:29:00
 */
public class LongArray {
  /** data array */
  private long[] data;
  /** Current size of array */
  private int size;
  /** Ratio of increase in capacity when the array expand. */
  private double grow_ratio = 1.5;
  /** Maximum spare space to allocate when array expand. */
  private int max_grow = 65536;

  public LongArray() {}

  public LongArray(int capacity) {
    setCapacity(capacity);
  }

  /** Set buffer size to given capacity. 
   * @param capacity new capacity 
   * @return this */
  public synchronized LongArray setCapacity(int capacity) {
    if (capacity < 0) throw new IllegalArgumentException();
    else if (capacity == 0) { data = null; return this; }
    // capacity > 0
    if (data == null) { data = new long[capacity]; return this; }
    if (data.length == capacity) return this;
    if (size > capacity) throw new IndexOutOfBoundsException();
    long[] temp = new long[capacity];
    System.arraycopy(data, 0, temp, 0, size);
    data = temp;
    return this;
  }

  /** Compact data buffer to currentMod size 
   * @return this */
  public synchronized LongArray compact() {
    return setCapacity(size);
  }

  /** Ensure the buffer has more then given capacity in byte. 
   * @param capacity min capacity */
  public synchronized void ensureCapacity(int capacity) {
    if (capacity < 0) throw new IllegalArgumentException();
    if (data == null) data = new long[capacity];
    else if (data.length < capacity)
      setCapacity((int)DataUtil.bound(0, capacity*grow_ratio, capacity+max_grow));
  }

  /** Put all data in given source to buffer 
   * @param data source
   * @return this */
  public synchronized LongArray put(long data) {
    ensureCapacity(size+1);
    this.data[size] = data;
    size++;
    return this;
  }

  /** Put all data in given source to buffer 
   * @param src source
   * @return this */
  public synchronized LongArray put(long[] src) {
    if (src == null) throw new NullPointerException();
    return put(src, 0, src.length);
  }

  /** Put given data starting from position with given length to buffer 
   * @param src source
   * @param position position to start copy from
   * @param length number of item to copy
   * @return this */
  public synchronized LongArray put(long[] src, int position, int length) {
    ensureCapacity(size + length);
    System.arraycopy(src, position, data, size, length);
    size += length;
    return this;
  }

  /** Get data buffer.  Remember get().length does NOT equals to size of content!
   * @return backing data
   * @see #size() */
  public synchronized long[] get() {
    return data;
  }

  public synchronized long get(int index) {
    return data[index];
  }

  public synchronized int size() {
    return size;
  }

  public synchronized int getCapacity() {
    return (data==null)?0:data.length;
  }
}
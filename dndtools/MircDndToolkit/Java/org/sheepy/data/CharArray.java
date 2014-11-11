package org.sheepy.data;

/**
 * An auto-expanding character array
 * Created by Ho Yiu Yeung on 04-Apr-2005 at 19:29:00
 */
public class CharArray {
  /** Character buffer */
  private char[] data;
  /** Current size of buffer */
  private int size;
  /** Ratio of increase in capacity when the array expand. */
  public int grow_ratio = 2;
  /** Maximum spare space to allocate when array expand. */
  public int max_grow = 65536;

  public CharArray() {}

  public CharArray(int capacity) {
    setCapacity(capacity);
  }

  /** Set buffer size to given capacity. 
   * @param capacity capacity of array
   * @return this */
  public synchronized CharArray setCapacity(int capacity) {
    if (capacity < 0) throw new IllegalArgumentException();
    else if (capacity == 0) { data = null; return this; }
    // capacity > 0
    if (data == null) { data = new char[capacity]; return this; }
    if (data.length == capacity) return this;
    if (size > capacity) throw new IndexOutOfBoundsException();
    char[] temp = new char[capacity];
    System.arraycopy(data, 0, temp, 0, size);
    data = temp;
    return this;
  }

  /** Compact data buffer to currentMod size 
   * @return this */
  public synchronized CharArray compact() {
    return setCapacity(size);
  }

  /** Ensure the buffer has more then given capacity in byte. 
   * @param capacity min capacity */
  public synchronized void ensureCapacity(int capacity) {
    if (capacity < 0) throw new IllegalArgumentException();
    if (data == null) data = new char[capacity];
    else if (data.length < capacity)
      setCapacity(Math.min(capacity*grow_ratio, capacity+max_grow));
  }

  /** Put all data in given source to buffer 
   * @param src source
   * @return this */
  public synchronized CharArray put(char[] src) {
    if (src == null) throw new NullPointerException();
    return put(src, 0, src.length);
  }

  /** Put given data starting from position with given length to buffer 
   * @param src source
   * @param position source position to start copy from
   * @param length number of char to copy
   * @return this */
  public synchronized CharArray put(char[] src, int position, int length) {
    ensureCapacity(size + length);
    System.arraycopy(src, position, data, size, length);
    size += length;
    return this;
  }

  /** Get data buffer.  Remember get().length does NOT equals to size of content!
   * @return current backing data
   * @see #size() */
  public synchronized char[] get() {
    return data;
  }

  public synchronized int size() {
    return size;
  }

  public synchronized int getCapacity() {
    return (data==null)?0:data.length;
  }

  public synchronized CharArray setSize(int size) {
    if (size < this.size) throw new IllegalArgumentException();
    this.size = size;
    return this;
  }
}
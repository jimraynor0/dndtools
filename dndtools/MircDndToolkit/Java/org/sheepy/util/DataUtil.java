/*
 * Created on 2004/3/9
 */
package org.sheepy.util;

import java.awt.Point;

/**
 * Primitive data utility.
 * @author Ho Yiu Yeung
 */
public class DataUtil {
  public static final Double zeroDouble = new Double(0.0);
  public static final Integer zeroInteger = new Integer(0);

  /**
   * Check whether a string is an integer.
   * @param str
   * @return true if the string can be parsed to integer
   */
  public static boolean isInteger(String str) {
    if (StringUtil.isEmpty(str)) return false;
    try {
      Integer.valueOf(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Find out range of value of an integer array.
   * Return null if array is null or is zero length.
   * @param array Array of integers.
   * @return a Point object with x as min value and y as max value.
   */
  public static Point rangeOf(int[] array) {
    if ((array==null)||(array.length <= 0)) return null;
    int min, max;
    min = max = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] < min) min = array[i];
      else if (array[i] > max) max = array[i];
    }
    return new Point(min, max);
  }

  /**
   * Normalize a value so it is within specified boundary.
   * @param min Minimum value.
   * @param value Value to be normalized.
   * @param max Maximum value.
   * @return min if value is smaller then min,
   *         max if value is greater, and value otherwise.
   */
  public static int bound(int min, int value, int max) {
    return Math.min(Math.max(min, value), max);
  }

  /**
   * Normalize a value so it is within specified boundary.
   * @param min Minimum value.
   * @param value Value to be normalized.
   * @param max Maximum value.
   * @return min if value is smaller then min,
   *         max if value is greater, and value otherwise.
   */
  public static double bound(double min, double value, double max) {
    return Math.min(Math.max(min, value), max);
  }

  /**
   * Check whether two or more objects are the same using Object.equals(Object).
   * Accept null input.
   * @param o1 Object 1
   * @param o2 Object 2
   * @return true if two objects are equals, or are both null.
   * @see Object#equals(java.lang.Object)
   */
  public static boolean equals(Object o1, Object o2) {
    if (o1 == o2) return true;
    else if (o1 != null) return o1.equals(o2);
    else return false;
  }/**/

  /**
   * Check whether two or more objects are the same using Object.equals(Object).
   * Accept null inputs.
   * @param firstObj Object
   * @param o Objects
   * @return true if all the objects are equals, or are all null.
   * @see Object#equals(java.lang.Object)
   */
  public static boolean equals(Object firstObj, Object ... o) {
    if (firstObj == null) {
      if (o == null) return true;
      for (Object obj : o)
        if (obj != null) return false;
      return true;
    } else {
      for (Object obj : o)
        if (!(firstObj == obj) && !(firstObj.equals(obj))) return false;
      return true;
    }
  }/**/
}

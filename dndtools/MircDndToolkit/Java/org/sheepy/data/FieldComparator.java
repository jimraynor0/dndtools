package org.sheepy.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.sheepy.util.StringUtil;

/**
 * A comparator that accepts a field name, and compare the field of given objects.
 * Created by Ho Yiu Yeung on 26-Apr-2005 at 13:29:50
 * @param <T> Type of object to compare
 */
public class FieldComparator<T extends Object> implements Comparator<T> {
  private String field;
  private List<String> moreFields;
  private boolean nullSmallest;
  private boolean reverse;

  public FieldComparator(String field, String ... fields) {
    setFields(field, fields);
  }

  public FieldComparator(boolean nullSmallest, boolean reverse, String field, String ... fields) {
    this.field = field;
    this.nullSmallest = nullSmallest;
    this.reverse = reverse;
    if (fields != null && fields.length > 0) {
      moreFields = new ArrayList<String>(fields.length);
      for (String s : fields) moreFields.add(s);
    }
  }

  /**
   * If field is not set or is not defined, return 0.
   * If the field is a comparable object, it will be compared.
   * Otherwise the field will be compared as string.
   *
   * Two objects of different class with the same field will return 0;
   * @param obj1
   * @param obj2
   * @return negative if obj1 < obj2, 0 if same, positive if obj1 > obj2
   * @see #setNullSmallest(boolean)
   */
  public int compare(T obj1, T obj2) {
    int result = compareByField(obj1, obj2, field, moreFields);
    return (reverse)?-result:result;
  }

  /**
   * Recursively compare two objects
   * @param obj1
   * @param obj2
   * @param fields Field to compare with
   * @return negative if obj1 < obj2, 0 if same, positive if obj1 > obj2
   */
  private int compare(T obj1, T obj2, List<String> fields) {
    if (fields.size() <= 0) return 0;
    String s = fields.remove(0);
    int result = compareByField(obj1, obj2, s, fields);
    if (result != 0)
      return result;
    else
      return compare(obj1, obj2, fields);
  }

  @SuppressWarnings("unchecked")
  private int compareByField(T obj1, T obj2, String field, List<String> more) {
    try {
      if (StringUtil.isEmpty(field) || (obj1 == null && obj2 == null)) return 0;

      Field f = null;
      try {
        if (obj1 != null) f = obj1.getClass().getField(field);
        else if (obj2 != null) f = obj2.getClass().getField(field);
      } catch (NoSuchFieldException e) { return 0; }
      if (f == null) return 0;

      Object o1 = f.get(obj1);
      String s1 = (o1==null)?null:o1.toString();
      Object o2 = f.get(obj2);
      String s2 = (o2==null)?null:o2.toString();

      if (o1 != null && o2 != null && o1 instanceof Comparable && o2 instanceof Comparable)
        try { return (((Comparable)o1).compareTo(o2));
        } catch (ClassCastException e) {}

      int result = 0;
      int nullvalue = (nullSmallest)?-1:1;
      if (s1 == null)
        result = (s2 == null)?0:-nullvalue;
      else if (s2 == null)
        result = nullvalue;
      else
        result = s1.compareTo(s2);
      if (result != 0 || more == null)
        return result;
      else
        return compare(obj1, obj2, new ArrayList<String>(more));
    } catch (IllegalAccessException e) {
      return 0;
    }
  }

  public String getField() {
    return field;
  }

  /**
   * Set the name of field to compare.
   * @param field First field to check
   * @param fields More field to check
   */
  public void setFields(String field, String ... fields) {
    this.field = field;
    if (fields != null && fields.length > 0) {
      moreFields = new ArrayList<String>(fields.length);
      for (String s : fields) moreFields.add(s);
    }
  }

  public boolean isNullSmallest() {
    return nullSmallest;
  }

  /**
   * If set to true, null will be smaller then any other values.
   * Otherwise it is larger then any other values.
   * @param nullSmallest
   */
  public void setNullSmallest(boolean nullSmallest) {
    this.nullSmallest = nullSmallest;
  }

  public boolean isReverse() {
    return reverse;
  }

  /**
   * If set to true, orders are reversed.
   * This is applied after sorting null values.
   * @param reverse
   */
  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }
}

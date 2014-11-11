package org.sheepy.util;

/**
 * @author Ho Yiu Yeung
 */
public class ArrayUtil {
  public static final char[] charAry = new char[0];
  public static final byte[] byteAry = new byte[0];
  public static final int[] intAry = new int[0];
  public static final double[] doubleAry = new double[0];
  public static final float[] floatAry = new float[0];
  public static final boolean[] booleanAry = new boolean[0];
  public static final String[] StringAry = new String[0];
  public static final Integer[] IntegerAry = new Integer[0];
  public static final Long[] LongAry = new Long[0];
  public static final Double[] DoubleAry = new Double[0];
  public static final Float[] FloatAry = new Float[0];
  public static final Boolean[] BooleanAry = new Boolean[0];

  /**
   * Convert an Integer array to an int array
   * @param ary Input
   * @return Output
   */
  public static int[] Integer2int(Integer[] ary) {
    if (ary == null) return null;
    int[] result = new int[ary.length];
    for (int i=0; i < ary.length; i++) result[i] = ary[i].intValue();
    return result;
  }

  /**
   * Convert a long array to a Long array
   * @param ary Input
   * @return Output
   */
  public static Long[] long2Long(long[] ary) {
    if (ary == null) return null;
    Long[] result = new Long[ary.length];
    for (int i=0; i < ary.length; i++) result[i] = ary[i];
    return result;
  }


}

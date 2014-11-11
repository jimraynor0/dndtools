/*
 * Created on 2004/3/8
 */
package org.sheepy.data;

/**
 * An integer rectangle data class.
 * @author Ho Yiu Yeung
 */
public class IntRectangle {
  /** Left position of rectangle */
  public int left;
  /** Right position of rectangle */
  public int right;
  /** Top position of rectangle */
  public int top;
  /** Bottom position of rectangle */
  public int bottom;
  
  /**
   * Combine into this rectangle the area of another.
   * @param rect Rectangle to merge with.
   * @return this.
   */
  public IntRectangle merge(IntRectangle rect) {
    left = Math.min(left, rect.left);
    right = Math.max(right, rect.right);
    top = Math.min(top, rect.top);
    bottom = Math.max(bottom, rect.bottom);
    return this;   
  }
  
  /**
   * Normalize the rectangle, so that left <= top and top <= bottom.
   * @return this.
   */
  public IntRectangle normalize() {
    if (left > right) {
      int temp = left;
      left = right;
      right = temp;
    }
    if (top > bottom) {
      int temp = top;
      top = bottom;
      bottom = temp;
    }
    return this;
  }
}

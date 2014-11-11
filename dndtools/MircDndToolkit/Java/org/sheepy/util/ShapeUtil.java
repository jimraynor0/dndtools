package org.sheepy.util;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * java.awt.Shape utility
 * @author Ho Yiu Yeung
 */
public class ShapeUtil {
  public static final Dimension emptyDimension = new Dimension(0,0);
  public static final Point2D.Double emptyPoint2DDouble = new Point2D.Double(0,0);

  /**
   * Create a polygon out of given coordinate in form of {x1, y1, x2, y2, x3, y3...}
   * @param points point coordinates
   * @return Created polygon, or an mepty polygon if points is null or empty.
   * @throws IllegalArgumentException if points is not even in size
   */
  public static Polygon createPolygon(int[] points) {
    if ((points==null)||(points.length<=0)) return new Polygon();
    if ((points.length % 2) != 0) throw new IllegalArgumentException("coordinate array must be even in size");
    int length = points.length/2;
    int[] x = new int[length];
    int[] y = new int[length];
    for (int i=0; i<length; i++) {
      x[i] = points[i*2];
      y[i] = points[(i*2)+1];
    }
    return new Polygon(x, y, length);
  }

  /**
   * Return true if the given dimension filteredResult in a zero or negative size rectangle.
   * @param width width of rectangle
   * @param height height of rectangle
   * @return true if the given dimension filteredResult in a zero or negative size rectangle.
   */
  public static boolean zeroSizeRect(int width, int height) {
    return ((width<=0)||(height<=0));
  }

  public static Rectangle rect2D2rect(Rectangle2D rect) {
    if (rect == null) return null;
    return new Rectangle((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
  }
}

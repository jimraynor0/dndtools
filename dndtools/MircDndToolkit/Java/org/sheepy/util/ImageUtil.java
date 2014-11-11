
/*
 * Created on 2004/2/24
 */
package org.sheepy.util;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.VolatileImage;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Utility to load and dice images.
 * @author Ho Yiu Yeung
 */
public class ImageUtil {
  /** Mother component of media tracker, to prevent exception */
  private static final MediaTracker tracker = new MediaTracker(new Component(){});
  /**
   * Load image file.
   * Return null if no image is loaded.
   * @param s Filename of the image
   * @return Image loaded.
   */
  public static Image getImage(String s) {
    if (s==null) return null;
    Image img = Toolkit.getDefaultToolkit().createImage(s);
    if (img == null) return null;
    try {
      synchronized(tracker) {
        tracker.addImage(img, 1);
        tracker.waitForID(1);
      }
      return img;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Load image file at given location.
   * Return null if no image is loaded.
   * @param s Filename of the image.
   * @return Image loaded.
   */
  public static BufferedImage getBufferedImage(String s) {
    return ImageToBufferedImage(getImage(s), BufferedImage.TYPE_INT_ARGB_PRE);
  }

  /**
   * Load image at given location.
   * Return null if no image is loaded.
   * @param s URL of the image.
   * @return Image loaded, or null if no image is loaded.
   */
  public static BufferedImage getBufferedImage(URL s) {
    BufferedImage img = ImageToBufferedImage(
        new ImageIcon(s).getImage(),
        BufferedImage.TYPE_INT_ARGB_PRE);
    return img;
  }

  /**
   * Convert image to BufferedImage
   * Return null if no image is provided.
   * @param img Image to convert
   * @param ImageType Type of Image
   * @return Result buffered image
   * @see java.awt.image.BufferedImage#BufferedImage(int, int, int)
   */
  public static BufferedImage ImageToBufferedImage(Image img, int ImageType) {
    if (img == null) return null;
    int iw = img.getWidth(null);
    int ih = img.getHeight(null);
    if ((iw <= 0) || (ih <= 0)) return null;
    BufferedImage result = new BufferedImage(iw, ih, ImageType);
    result.createGraphics().drawImage(img,0,0,null);
    return result;
  }

  /**
   * Dice an image into equal portion.
   * Return null if image is null or is empty.
   * Start with top left and dice the largest equal size chunk possible.
   * @param img Image to dice.
   * @param row Row to dice into.
   * @param col Coloum to dice into.
   * @return Image neatly diced into required number.
   * @throws IllegalArgumentException If row or col < 0
   * @throws ImagingOpException If image is too small to be diced.
   */
  public static BufferedImage[][] diceImage(BufferedImage img, int row, int col) {
    if (img == null) return null;
    int w, h;
    w = img.getWidth();
    h = img.getHeight();
    if ((w<0)||(h<0)) return null;
    if((row < 0)||(col < 0)) throw new IllegalArgumentException();
    w = w / col;
    h = h / row;
    if ((w<0)||(h<0)) throw new ImagingOpException("Image too small to be diced");
    BufferedImage[][] result = new BufferedImage[row][col];
    for (int y=0; y < row; y++)
      for (int x=0; x < col; x++)
        result[y][x] = img.getSubimage(x*w, y*h, w, h);
    return result;
  }

  /**
   * Dice an image into equal portion.
   * Return null if image is null or is empty.
   * @param img Image to dice.
   * @param left Left position to start dicing.
   * @param top Top position to start dicing.
   * @param width Width of diced chunk.
   * @param height Height of diced chunk.
   * @param row Row to dice into.
   * @param col Coloum to dice into.
   * @return Image neatly diced according to specification.
   * @throws IllegalArgumentException If left, top, width, height, row or col < 0
   * @throws ImagingOpException If image is too small to be diced.
   */
  public static BufferedImage[][] diceImage(
      BufferedImage img, int left, int top,
      int width, int height, int row, int col) {
    if (img == null) return null;
    if (((left<0)||(top<0)||(left>=img.getWidth())||(top>=img.getHeight()))||
        ((width<1)||(height<1)||(row<1)||(col<1)))
          throw new IllegalArgumentException();
    return diceImage(img.getSubimage(left, top, width*col, height*row),row,col);
  }

  /**
   * Load image file to a volatile image compatible with given display mode.
   * Return null if no image is loaded.
   * @param s Filename of the image.
   * @param c Visible component in desired compatible environment.
   * @return Image loaded, or null if no image is loaded.
   */
  public static VolatileImage getVolatileImage(String s, Component c) {
    return ImageToVolatileImage(new ImageIcon(s).getImage(), c);
  }

  /**
   * Convert image to a volatile one compatible with given display mode.
   * Return null if no image is provided.
   * If c is null or headless, create one compatible with default configuration.
   * @param img Image to convert.
   * @param c Visible component in desired compatible environment.
   * @return Image loaded, or null if no image is loaded.
   */
  public static VolatileImage ImageToVolatileImage(Image img, Component c) {
    if (img == null) return null;
    int iw = img.getWidth(null);
    int ih = img.getHeight(null);
    if ((iw <= 0) || (ih <= 0)) return null;
    VolatileImage result = (c!=null)?c.createVolatileImage(iw, ih):null;
    if (result == null) result =
      GraphicsEnvironment.getLocalGraphicsEnvironment().
      getDefaultScreenDevice().getDefaultConfiguration().
      createCompatibleVolatileImage(iw, ih);
    result.createGraphics().drawImage(img,0,0,null);
    return result;
  }

  /**
   * Add size of an image to given rectangle.
   * @param img
   * @param rect
   */
  public static void addImgSizeToRectangle(Image img, Rectangle rect) {
    if (img == null) return;
    rect.add(img.getWidth(null), img.getHeight(null));
  }

  private static Image dummy;
  /**
   * Return a shared one-pixel transparent black image
   * @return a shared one-pixel transparent black image
   */
  public static Image getDummyImage() {
    if (dummy == null) dummy = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
    return dummy;
  }

  private static ImageIcon dummyIcon;
  /**
   * Return a shared one-pixel transparent black image icon
   * @return a shared one-pixel transparent black image icon
   */
  public static ImageIcon getDummyIcon() {
    if (dummyIcon == null) dummyIcon = new ImageIcon(getDummyImage());
    return dummyIcon;
  }
}

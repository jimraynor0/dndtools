package res.img;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class serves no purpose other then loading image resources
 * Created by Ho Yiu Yeung on 29-Aug-2005 at 06:43:46
 */
public class ImagePath {

  /**
   * Get an imageicon of given name from same path of this class
   * @param name Image file name
   * @return loaded ImageIcon
   * @throws IOException If icon can't be loaded for any reason
   */
  public static ImageIcon loadIcon(String name) throws IOException {
    InputStream in = ImagePath.class.getResourceAsStream(name);
    if (in == null) throw new IOException("Image resource "+name+" not found");
    return new ImageIcon(ImageIO.read(in));
  }

}

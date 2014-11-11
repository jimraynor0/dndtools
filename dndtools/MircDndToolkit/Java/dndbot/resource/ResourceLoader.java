package dndbot.resource;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Help to load resource
 */
public class ResourceLoader {

  private static final String pack = ResourceLoader.class.getPackage().getName()+".";

  /**
   * Load a resource of given basename in same folder using current locale
   * @param baseName module base name
   * @param locale locale to get bundle of
   * @return resource bundle of (baseName+"Resource") of given locale in same pack with this class
   */
  public static final ResourceBundle getBundle(String baseName, Locale locale) {
//    if (loader != null) return ResourceBundle.getBundle( baseName, locale, loader);
//    else
    return ResourceBundle.getBundle(pack+baseName, locale);
  }
  
  public static InputStream getResource(String filename) {
    return ResourceLoader.class.getResourceAsStream(filename);
  }
}

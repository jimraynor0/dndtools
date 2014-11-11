package org.sheepy.data.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Created by Ho Yiu Yeung on 25-Jul-2005 at 06:19:53
 */
public class BasicFileFilter extends FileFilter {
  private String desc;
  private String[] extensions;
  public static final String EXTENSION_PLACEHOLDER = "($EXT)";

  /**
   * Create a simple file filter that allows given extensions
   * @param extensions Extension seprated by ';', in lower case without dot (e.g. "jpg;jpeg;png;gif;bmp").
   * @param desc Description of this filter.  If null or if contains "($EXT)", a list of extension will be used/inserted.
   */
  public BasicFileFilter(String extensions, String desc) {
    if (desc == null) desc = EXTENSION_PLACEHOLDER;
    this.desc = desc;
    this.extensions = extensions.split(";");
    if (desc.contains(EXTENSION_PLACEHOLDER)) {
      StringBuilder ext = new StringBuilder(this.extensions.length*7+2).append('(');
      for (String s : this.extensions) {
        if (ext.length() > 1) ext.append(", ");
        ext.append("*.").append(s);
      }
      ext.append(')');
      this.desc = desc.replace(EXTENSION_PLACEHOLDER, ext);
    }
  }

  public boolean accept(File pathname) {
    if (pathname.isHidden()) return false; // Hide hidden
    if (pathname.isDirectory()) return true; // Show directory
    if (!pathname.isFile()) return false; // Hide non-file that's not directory
    for (String s : extensions)
      if (pathname.toString().toLowerCase().endsWith("."+s)) return true;
    return false; // No match.
  }

  public String getDescription() {
    return desc;
  }

}

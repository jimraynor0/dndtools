package org.sheepy.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sheepy.util.ImageUtil;
import org.sheepy.util.StringUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Represent a relative path to file resources.
 * Contains methods that allows loading resources and set global override.
 */
public class FileResources {
  /** Path of this resource */
  private String path;
  /** Relative path to application root  */
  // unused since jar loading is not supported
  //private static final String rootpath = "/"+StringUtil.repeatString(1 + StringUtil.countOccurence(FileResources.class.getPackage().getName(), '.'), "../");

  /**
   * Create a resources at application path
   */
  public FileResources() {  }

  /**
   * Create a resources at given path
   * @param path Path of the resource
   */
  public FileResources(String path) {
    if (!StringUtil.isEmpty(path)&&(!path.endsWith("/"))) path = path+'/';
    this.path = path;
  }

  /**
   * Create a new resource that is in a subpath of the resource
   * @param subpath Subpath of new resource relative to this resource
   * @return New resource at subpath relative to this resource
   */
  public FileResources subPath(String subpath) {
    return new FileResources(path + subpath);
  }

  /**
   * Expand given file to include full path relative to application root.
   * The search order is: override folder, folder
   * @param name Name of file
   * @return Relative path and name to an existing file.
   */
  public String expandFilename(String name) {
    if (StringUtil.isEmpty(name)) return null;
    // check override
    if (override_path!= null) {
      String override = override_path + new File(name).getName();
      if (new File(override).exists()) return override;
    }
    // Normal loading
    {
      String full = path + name;
      if (new File(full).exists()) return full;
    }
    return null;
  }

  /**
   * Load a file url from the path.
   * Base on implementation of expandFilename(String)
   * @param name Name of file
   * @return Loaded file, or null if unsuccessful.
   * @see FileResources#expandFilename(java.lang.String)
   */
  public URL loadURL(String name) {
    File f = loadFile(name);
    try {
      return (f == null)?null:f.toURI().toURL();
    } catch (MalformedURLException e) {e.printStackTrace(); return null;}
  }

  /**
   * Load a file from the path.
   * Base on implementation of expandFilename(String)
   * @param name Name of file
   * @return Loaded file, or null if unsuccessful.
   * @see FileResources#expandFilename(java.lang.String)
   */
  public File loadFile(String name) {
    String s = expandFilename(name);
    return (s==null)?null:new File(name);
  }

  /**
   * Load image of given name.
   * Non-existing file filteredResult in a size-1 transparent black image.
   * Base on implementation of expandFilename(String).
   * @param name Name of file
   * @return Loaded image.
   * @see FileResources#expandFilename(java.lang.String)
   */
  public ImageIcon loadImageIcon(String name) {
    String s = expandFilename(name);
    return (s==null)?ImageUtil.getDummyIcon():new ImageIcon(s);
  }

  public Document loadXML(String name) {
    String s = expandFilename(name);
    if (s==null) return null;
    Document d = null;
    try {
      d = getXMLBuilder().parse(new File(s).toURI().toURL().toString());
    } catch (SAXException e) { e.printStackTrace();
    } catch (IOException e) { e.printStackTrace();
    }
    return d;
  }

  /**
   * Default XMl builder factory.  Can be overridden.
   */
  public static DocumentBuilder xmlBuilder;
  /**
   * Get default DocumentBuilderFactory to be used, creating one if necessary.
   * By default, ignore comment and whitespace and create text element.
   * @return shared DocumentBuilderFactory
   */
  public static DocumentBuilder getXMLBuilder() {
    if (xmlBuilder != null) return xmlBuilder;
    DocumentBuilderFactory factory;
    factory = DocumentBuilderFactory.newInstance();
    factory.setCoalescing(true);
    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(true);
    try {
      xmlBuilder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      try {
        xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      } catch (ParserConfigurationException e1) {
        e1.printStackTrace();
      }
    }
    return xmlBuilder;
  }


  /** Path for override path of all resources. */
  private static String override_path = "override/";

  /**
   * Set the override path.  Files in override path precedes all normal resources.
   * The path is ralative, and functions in both jar and real file tree.
   * @param path override path
   */
  public static void setOverridePath(String path) {
    if (!StringUtil.isEmpty(path)&&!path.endsWith("/")) path = path+'/';
    override_path = path;
  }

  /**
   * Return the override path.
   * @return override path
   */
  public static String getOverridePath() { return override_path; }
}

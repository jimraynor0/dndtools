package org.sheepy.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Load data from a Zip / Jar file.  Can cache data.
 * Based on a similiar tool originally written by Behrouz Fallahi, acquired from devx.com.
 */
public class ZipExtractor {
  private final Map<String, byte[]> contents = new HashMap<String, byte[]>(50);
  private final Map<String, Integer> entrySizes = new HashMap<String, Integer>(50);

  private String m_jarFileName;

  public ZipExtractor(String jarFileName) throws IOException {
    this.m_jarFileName = jarFileName;
    initialise();
  }

  /** Return true if the archive contains given file. 
   * @param name filename
   * @return existance of file */
  public boolean contains(String name) {
    return (entrySizes.containsKey(name));
  }

  /**
   * Get data of given file in byte array.  Data is first loaded to memory cache.
   * May throws FileNotFoundException if files does not exists.
   *
   * To preload a batch of file, see #loadFilesStartWith(String) and
   * #loadFiles(String).  To load a big file without loading it fully
   * in cache, use #getDirectDataStream(String)
   * @param name filename
   * @return file content in byte
   * @throws IOException 
   */
  public byte[] getData(String name) throws IOException {
    return getData(name, false);
  }

  /**
   * Get data of given file in byte array.  Data is first loaded to memory cache.
   * May throws FileNotFoundException if files does not exists.
   * 
   * @param name filename
   * @param noCache if true, does not keep data in cache.
   * @return content of file
   * @throws IOException 
   */
  public byte[] getData(String name, boolean noCache) throws IOException {
    byte[] result = null;
    if (contents.containsKey(name)) result = (byte[]) contents.get(name);
    loadFiles(name);
    if (contents.containsKey(name)) result = (byte[]) contents.get(name);
    if (noCache) contents.remove(name);
    if (result != null) return result;
    throw new FileNotFoundException();
  }

  /**
   * Get byte array input stream of given file.  Data is first loaded to memory cache.
   * May throws FileNotFoundException if files does not exists.
   *
   * To preload a batch of file, see #loadFilesStartWith(String) and
   * #loadFiles(String).  To load a bug file without loading it fully
   * in cache, use #getDirectDataStream(String)
   * 
   * @param name filename 
   * @return InputStream of the file
   * @throws IOException 
   */
  public ByteArrayInputStream getDataStream(String name) throws IOException {
    return getDataStream(name, false);
  }

  /**
   * Get byte array input stream of given file.  Data is first loaded to memory cache.
   * May throws FileNotFoundException if files does not exists.
   * 
   * @param name filename
   * @param noCache if true, does not keep data in cache.
   * @return InputStream of the file
   * @throws IOException 
   */
  public ByteArrayInputStream getDataStream(String name, boolean noCache) throws IOException {
    byte[] data = getData(name);
    if (noCache) contents.remove(name);
    return new ByteArrayInputStream(data);
  }

  /** Load files that begins with given pattern. 
   * @param prefix filename prefix
   * @throws IOException */
  public void loadFilesStartWith(String prefix) throws IOException {
    loadFiles(prefix+".*");
  }

  /** Load file bypassing cache, directly from the Zip/Jar file.  For loading large files. 
   * @param name filename
   * @return Direct InputStream
   * @throws IOException */
  public InputStream getDirectDataStream(String name) throws IOException {
    if (name == null) throw new NullPointerException();
    ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(m_jarFileName)));
    ZipEntry entry = null;
    while ((entry = in.getNextEntry()) != null)
      if (name.equals(entry.getName())) return in;
    throw new FileNotFoundException();
  }

  /**
   * Uncompress and load resources from Jar that matches given pattern into memory cache.
   * A pattern of null will load the whole file.
   * return number of loaded files
   * 
   * @param pattern Filename pattern
   * @return number of loaded files
   * @throws IOException 
   */
  public int loadFiles(String pattern) throws IOException {
    Matcher filter = pattern == null ? null : Pattern.compile(pattern).matcher("");
    ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(m_jarFileName)));
    ZipEntry entry = null;
    int filecount = 0;
    while ((entry = in.getNextEntry()) != null) {
      // Skip directory, loaded files, and filter unwanted stuffs
      String name = entry.getName();
      if (entry.isDirectory() || contents.containsKey(name)) continue;
      if (filter != null) {
        filter.reset(name);
        if (!filter.matches()) continue;
      }
      // Get file size and allocate buffer
      int size = (int) entry.getSize();
      if (size < 0) size = entrySizes.get(entry.getName());
      byte[] b = new byte[size];
      // Actual file loading.  Try to read as much as possible until finish loading.
      int start = 0;
      while (size > 0) {
        int read = in.read(b, start, size);
        if (read <= 0) break;
        size -= read;
        start += read;
      }
      in.closeEntry();
      filecount++;
      contents.put(name, b);
    }
    in.close();
    return filecount;
  }

  /**
   * Load the file table
   * @throws IOException 
   */
  private void initialise() throws IOException {
    ZipFile file = null;
    try {
      file = new ZipFile(m_jarFileName);
      Enumeration e = file.entries();
      while (e.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) e.nextElement();
        entrySizes.put(entry.getName(), new Integer((int) entry.getSize()));
      }
      file.close();
      file = null;
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  /**
   * Unload all loaded content to free memory
   */
  public void unloadAll() {
    contents.clear();
  }

  /**
   *  Unload some loaded content to free memory.
    * @param pattern  Pattern of name of content to unload
   */
  public void unload(String pattern) {
    if (pattern == null) unloadAll();
    Matcher filter = Pattern.compile(pattern).matcher("");
    for (String key : contents.keySet()) {
      filter.reset(key);
      if (filter.matches()) contents.remove(key);
    }
  }
}
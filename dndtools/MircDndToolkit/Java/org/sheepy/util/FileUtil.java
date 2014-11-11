package org.sheepy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * File utility
 * @author Yenug Ho Yiu
 */
public class FileUtil {
  /**
   * Copy in file to out file
   * @param in File to copy from
   * @param out File to copy to
   * @throws IOException 
   */
  public void copyFile(File in, File out) throws IOException {
    FileChannel sourceChannel = new FileInputStream(in).getChannel();
    FileChannel destinationChannel = new FileOutputStream(out).getChannel();
    sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
    sourceChannel.close();
    destinationChannel.close();
  }

  /**
   * Create a BufferedReader
   * @param f File to read from
   * @param locale Local of file.  Can be empty or null, in which case default locale is used
   * @return BufferedReader of the file
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException 
   */
  public static BufferedReader getFileInBuffer(File f, String locale) throws FileNotFoundException, UnsupportedEncodingException {
    if (StringUtil.isEmpty(locale))
      return new BufferedReader(new InputStreamReader(new FileInputStream(f)));
    else
      return new BufferedReader(new InputStreamReader(new FileInputStream(f), locale));
  }

  /**
   * Create a buffered writer from given file.
   * @param f File to write to
   * @param locale Local of file.  Can be empty or null, in which case default locale is used
   * @param append Append to end of file?
   * @return BufferedWriter of the file
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException 
   */
  public static BufferedWriter getFileOutBuffer(File f, String locale, boolean append) throws FileNotFoundException, UnsupportedEncodingException {
    if (StringUtil.isEmpty(locale))
      return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, append)));
    else
      return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, append), locale));
  }

  /**
   * Get a buffered PrintWrtier from given file
   * @param f File to write to
   * @param locale Local of file.  Can be empty or null, in which case default locale is used
   * @param append Append to end of file?
   * @return Buffrered PrintWriter of the file
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException 
   */
  public static PrintWriter getFileOutPrint(File f, String locale, boolean append) throws FileNotFoundException, UnsupportedEncodingException {
    return new PrintWriter(getFileOutBuffer(f, locale, append));
  }

  /**
   * Read a whole file into a character sequence.
   * UNTESTED
   * @param f File to read
   * @param locale File locale
   * @return Given file in string, or null if anything goes wrong.
   */
  public static String asString(File f, String locale) {
    try {
      if ((f == null)||(!f.isFile())) return null;
      long l = (int)f.length();
      if (l > Integer.MAX_VALUE) return null;
      else if (l <= 0) return "";
      char[] c = new char[(int)l];
      BufferedReader in = getFileInBuffer(f, locale);
      if (in.read(c, 0, c.length) < 0) return null;
      return new String(c);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Read a whole file into String
   * @param f File to read
   * @param locale Locale of the file
   * @return Given file in string, or null if anything goes wrong.
   * @throws IOException 
   */
  public static List<String> asStringList(File f, String locale) throws IOException {
    List<String> list = new ArrayList<String>(100);
    BufferedReader in = getFileInBuffer(f, locale);
    while (in.ready()) {
      String line = in.readLine();
      if (line.length() <= 0) continue;
      list.add(line);
    }
    return list;
  }
}

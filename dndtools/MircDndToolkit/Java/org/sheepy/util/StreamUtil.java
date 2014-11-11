package org.sheepy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * Created by Ho Yiu Yeung on 27-Jul-2005 at 00:55:29
 */
public class StreamUtil {

  public static final int DEFAULT_STREAM_BLOCK_SIZE = 16 * 1024;

  /**
   * Copy from in to out, using a buffer size of #DEFAULT_STREAM_BLOCK_SIZE.
   * Output stream will be flushed when copying is complete.
   * @param in Input stream to copy from
   * @param out Output stream to copy to
   * @throws IOException if anything goes wrong.
   */
  public static void copyStream(InputStream in, OutputStream out) throws IOException {
    copyStream(in, out, DEFAULT_STREAM_BLOCK_SIZE, true);
  }

  /**
   * Copy from in to out, using given buffer size
   * @param in Input stream to copy from
   * @param out Output stream to copy to
   * @param block_size Buffer size.  Must be larger then zero.
   * @param flush If true, flush output stream after copying is complete
   * @throws IOException if anything goes wrong.
   */
  public static void copyStream(InputStream in, OutputStream out, int block_size, boolean flush) throws IOException {
    int size = in.available();
    if (size <= 0) return;
    if (size < block_size) block_size = size;
    // Start copying
    byte[] buf = new byte[block_size];
    while (in.available() > 0) {
      int read = in.read(buf);
      if (read <= 0) break;
      out.write(buf, 0, read);
    }
    if (flush) out.flush();
  }
  
  public static String readAllAsString(BufferedReader in) throws IOException {
    StringBuilder buf = new StringBuilder();
    while (in.ready()) buf.append(in.readLine()).append("\n");
    buf.setLength(buf.length()-1);
    return buf.toString();
  }
  
  public static String readAllAsString(Reader in) throws IOException {
    return readAllAsString(new BufferedReader(in));
  }

  public static String readAllAsString(InputStream in) throws IOException {
    return readAllAsString(new BufferedReader(new InputStreamReader(in)));
  }

  public static String readAllAsString(InputStream in, Charset charset) throws IOException {
    return readAllAsString(new BufferedReader(new InputStreamReader(in, charset)));
  }

  /**
   * Read a byte array from an input stream
   * @param in
   * @param block_size
   * @return
   * @throws IOException
   *
  public static byte[] streamToArray(InputStream in, int block_size, boolean closeStream) throws IOException {
  int size = in.available();
  if (size <= 0) return ArrayUtil.byteAry;
  if (size < block_size) block_size = size;
  // Start copying

  byte[] buf = new byte[block_size];
  while (in.available() > 0) {
  int read = in.read(buf);
  if (read <= 0) break;
  out.write(buf, 0, read);
  }
  if (closeStream) in.close();
  }/**/
}

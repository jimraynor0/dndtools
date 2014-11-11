package org.sheepy.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

/**
 * An input stream that reads in from a reader.
 *
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/9
 */
public class ReaderInputStream extends InputStream {
  private final Reader r;
//  private final Charset c;
//  private volatile Integer last;

  public ReaderInputStream(Reader reader) {
    if (reader == null) throw new IllegalArgumentException();
    r = reader;
//    c = null;
  }

//  public ReaderInputStream(Reader r) {
//    if (r == null || c == null) throw new IllegalArgumentException();
//    this.r = r;
//    this.c = c;
//  }

  public int read() throws IOException {
//    if (c == null)
      return r.read();
//    else {
//      if (last != null) {
//        int i = last;
//        last = null;
//        return i;
//      } else {
//        IntBuffer buf = c.encode(new String(new int[]{r.read()}, 0, 1)).asIntBuffer();
//        if (buf.position() > 1) last = buf.get(1);
//        return buf.get(0);
//      }
//    }
  }

  public Reader getReader() {
    return r;
  }

  public int read(byte b[], int off, int len) throws IOException {
    if (len == 0) return 0;
    if (off < 0 || len < 0 || off+len > b.length) throw new IndexOutOfBoundsException();
    ByteBuffer buf = ByteBuffer.wrap(b, off, len);
    return r.read(buf.asCharBuffer());
  }

  public long skip(long n) throws IOException {
    return r.skip(n);
  }

  /**
   * Return 1 if reader is ready to be read.
   * @return 1 if underlying reader is ready to be read without blocking, 0 otherwise
   */
  public int available() throws IOException {
    return 0;
    //return (r.ready()) ? 1 : 0;
  }

  /**
   * Mark a position in underlying reader to reset to.
   * @param readlimit position of marker
   * @throws RuntimeException if exception happens when setting reader's mark
   */
  public synchronized void mark(int readlimit) {
    try {
      r.mark(readlimit);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized void reset() throws IOException {
    r.reset();
  }

  public boolean markSupported() {
    return r.markSupported();
  }

  public void close() throws IOException {
    r.close();
  }
}

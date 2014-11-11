package org.sheepy.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * An OutputStream that outputs to a Writer.
 *
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/7
 */
public class WriterOutputStream extends OutputStream {
  private final Writer writer;
  private final Charset charset;
  private final ByteBuffer buf = ByteBuffer.allocate(4);

  public WriterOutputStream(Writer writer) {
    this(writer, null);
  }

  /**
   * Create a WriterOutputStream that decode stream from given charset to unicode before forwarding to writer
   * @param writer
   * @param charset
   */
  public WriterOutputStream(Writer writer, Charset charset) {
    if (writer == null) throw new NullPointerException();
    this.writer = writer;
    this.charset = charset;
  }

  public void write(int b) throws IOException {
    if (charset == null)
      writer.write(b);
    else {
      CharBuffer f = null;
      synchronized (buf) {
        buf.clear();
        buf.putInt(b);
        buf.flip();
        f = charset.decode(buf);
      }
      writer.write(f.array(), 0, f.length());
    }
  }

  public void write(byte b[], int off, int len) throws IOException {
    if (charset == null) {
      writer.write(new String(b, off, len));
    } else {
      writer.write(new String(b, off, len, charset.name()));
    }
  }

  public Writer getWriter() {
    return writer;
  }

  public Charset getCharset() {
    return charset;
  }

  public void close() throws IOException {
    writer.close();
  }
}

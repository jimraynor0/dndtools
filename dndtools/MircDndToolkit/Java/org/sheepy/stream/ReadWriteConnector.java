package org.sheepy.stream;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.io.Writer;

/**
 * A connector that connects a reader and a writer
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/7
 */
public class ReadWriteConnector {
  private final Reader in;
  private final Writer out;
  private final int bufferSize;
  private InputToOutputCopier process;

  public ReadWriteConnector(Reader in, Writer out) {
    this(in, out, 256);
  }

  public ReadWriteConnector(Reader in, Writer out, int buffersize) {
    this.in = in;
//    read = Channels.newChannel(in);
    this.out = out;
//    write = Channels.newChannel(out);
    bufferSize = buffersize;
    process = new InputToOutputCopier();
    process.start();
  }

  public Reader getReader() {
    return in;
  }

  public Writer getWriter() {
    return out;
  }

  /** Stops the connector. */
  @SuppressWarnings("deprecation")
  public synchronized void stop() {
    process.stop = true;
    process.stop();
    process = null;
  }


  /** Resume connector after stop. */
  public synchronized void resume() {
    if (process != null) return;
    process = new InputToOutputCopier();
    process.start();
  }

  /** The actual thread that copies streams */
  private class InputToOutputCopier extends Thread {
    private final char[] buf;
    private volatile boolean stop;

    public InputToOutputCopier() {
      super();
      buf = new char[bufferSize];
    }

    public void run() {
      while (!stop) try {
        int i = in.read(buf);
        if (i > 0) {
          out.write(buf, 0, i);
          out.flush();
        } else {
          synchronized (this) {
            wait(1000);
          }
        }
      } catch (InterruptedIOException e) {
      } catch (InterruptedException e) {
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }
  }
}

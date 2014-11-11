package org.sheepy.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Connect an InputStream and OutputStream that reads from an InputStream.
 * It is automically closed when the InputStream is closed
 *
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/7
 */
public class StreamConnector {
  private final InputStream in;
  private final OutputStream out;
//  private final ReadableByteChannel read;
//  private final WritableByteChannel write;
  private final int bufferSize;
  private InputToOutputCopier process;

  public StreamConnector(InputStream in, OutputStream out) {
    this(in, out, 256);
  }

  public StreamConnector(InputStream in, OutputStream out, int buffersize) {
    this.in = in;
//    read = Channels.newChannel(in);
    this.out = out;
//    write = Channels.newChannel(out);
    bufferSize = buffersize;
    process = new InputToOutputCopier();
    process.start();
  }

  public InputStream getInputStream() {
    return in;
  }

  public OutputStream getOutputStream() {
    return out;
  }

  /** Stops the connector. */
  @SuppressWarnings("deprecation")
  public synchronized void stop() {
    process.stop = true;
    process.stop(); // Must use stop to stop InputStream.read
//    process.interrupt();
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
//    private final ByteBuffer buffer;
    private final byte[] buf;
    private volatile boolean stop;

    public InputToOutputCopier() {
      super();
//      buffer = ByteBuffer.allocate(bufferSize);
      buf = new byte[bufferSize];
    }

    public void run() {
      while (!stop) {
        try {
          int i = in.read(buf);
          if (i > 0) out.write(buf, 0, i);
//          read.read(buffer);
//          if (buffer.position() > 0) {
//            buffer.flip();
//            write.write(buffer);
//            buffer.clear();
//          }
//          else
//            wait(1000);
        } catch (IOException e) {
          e.printStackTrace();
          return;
//        } catch (NonReadableChannelException e) {
//          e.printStackTrace();
//          return;
//        } catch (InterruptedException e) {
        }
      }
    }
  }
}

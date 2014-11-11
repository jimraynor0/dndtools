package org.sheepy.stream;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.Reader;

import javax.swing.text.JTextComponent;

/**
 * A reader that reads from a JTextComponent.
 * When enter is pressed, the text is read from component to a buffer, where it can be read by this stream.
 *
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/7
 */
public class JTextComponentReader extends Reader {
  private final JTextComponent text;
  private final StringBuilder buffer = new StringBuilder();
  private final KeyListener l;

  public JTextComponentReader(JTextComponent text) {
    super();
    this.text = text;
    l = new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
          synchronized(buffer) {
            buffer.append(JTextComponentReader.this.getText().getText()).append(System.getProperty("line.separator"));
            buffer.notifyAll();
          }
          JTextComponentReader.this.getText().setText("");
        }
      }
    };
    text.addKeyListener(l);
  }

  public int read(char cbuf[], int off, int len) throws IOException {
    int i = 0;
    synchronized (buffer) {
      if (buffer.length() <= 0) try {
        buffer.wait();
      } catch (InterruptedException e) {}
      i = Math.min(len-off, buffer.length());
      if (i > 0) {
        buffer.getChars(0, i, cbuf, off);
        buffer.delete(0, i);
      }
    }
    return i;
  }

  public long skip(long n) throws IOException {
    int i = 0;
    synchronized (buffer) {
      if (buffer.length() <= 0) try {
        buffer.wait();
      } catch (InterruptedException e) {}
      i = (int) Math.min(n, buffer.length());
      if (i > 0) buffer.delete(0, i);
    }
    return i;
  }

  public synchronized boolean ready() throws IOException {
    return buffer.length() > 0;
  }

  public boolean markSupported() {
    return false;
  }

  public void close() throws IOException {
  }

  public JTextComponent getText() {
    return text;
  }
}

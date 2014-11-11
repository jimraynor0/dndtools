package org.sheepy.stream;

import java.io.IOException;
import java.io.Writer;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * An output stream that streams to a Swing Document.
 * This class should be thread safe.
 *
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/7
 */
public class DocumentWriter extends Writer {
  private final AbstractDocument doc;
  private final AttributeSet a;

  /**
   * Create a write that append characters to given document
   * @param doc Document to write to
   */
  public DocumentWriter(AbstractDocument doc) {
    this(doc, null);
  }

  /**
   * Create a write that append characters to given textarea.
   * The text area MUST use an AbstractDocument or its subclass as document.
   * @param txt TextArea to write to
   */
  public DocumentWriter(JTextArea txt) {
    this((AbstractDocument) txt.getDocument(), null);
  }

  /**
   * Create a write that append characters to given document with specified attribute
   * @param doc Document to write to
   * @param a Attribute of written string
   */
  public DocumentWriter(AbstractDocument doc, AttributeSet a) {
    super();
    if (doc == null) throw new NullPointerException();
    this.doc = doc;
    this.a = a;
  }

  public AbstractDocument getDocument() {
    return doc;
  }

  public AttributeSet getAttributeSet() {
    return a;
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    final String s = new String(cbuf, 0, len);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
//          synchronized(doc) {
//            int i = doc.getLength();
            doc.insertString(doc.getLength(), s, getAttributeSet());
//          }
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void flush() throws IOException {
  }

  public void close() throws IOException {
  }
}

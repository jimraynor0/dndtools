package org.sheepy.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * An adapter class for DocumentListener.  Just override needed methods.
 * Created by Ho Yiu Yeung on 24-Jul-2005 at 04:49:29
 */
public abstract class DocumentAdapter implements DocumentListener {
  /**
   * Invoke contentUpdate by default
   * @see #contentUpdate
   * @param e
   */
  public void insertUpdate(DocumentEvent e) {
    contentUpdate(e);
  }

  /**
   * Invoke contentUpdate by default
   * @see #contentUpdate
   * @param e
   */
  public void removeUpdate(DocumentEvent e) {
    contentUpdate(e);
  }

  /**
   * Indicates that content has been inserted or removed.
   * Won't work if other two updates are overriden.
   * @param e
   */
  public abstract void contentUpdate(DocumentEvent e);

  public void changedUpdate(DocumentEvent e) {
  }
}

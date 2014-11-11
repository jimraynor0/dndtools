package org.sheepy.util;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Swing Dialog (JOptionPane) utility class
 * Provides methods to produce common dialogs
 *
 * It is advised that ParentComponent is set before calling other functions
 *
 * @author Ho Yiu Yeung "Sheepy"
 * @since 2006/5/5
 */
public class DialogUtil {

  private static Component c;
  public static void setParentConmponent(Component comp) {
    c = comp;
  }

  public static void showErrorDialog(String message, String title) {
    JOptionPane.showMessageDialog(c, message, title, ERROR_MESSAGE);
  }
}

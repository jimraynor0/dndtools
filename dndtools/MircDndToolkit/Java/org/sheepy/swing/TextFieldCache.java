package org.sheepy.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JTextField;

/**
 * Construct a cache navigatable with up/down over a text field
 *
 * Created by Ho Yiu YEUNG on 2007 Apr 10
 */
public class TextFieldCache extends KeyAdapter implements ActionListener {
  private final java.util.List<String> cache; // current cache
  private int commandIndex = 0; // index of command
  private JTextField textField; // text field being cached
  private int limit; // cache size limit
  private boolean newCommand; // Is the current text a new command?


  /**
   * Create a command cache with a default cache size of 99 line
   * @param target Target text field to add cache
   */
  public TextFieldCache(JTextField target) {
    this(target, 99);
  }


  /**
   * Create a command cache
   * @param target Target text field to add cache
   * @param limit Cache size limit
   */
  public TextFieldCache(JTextField target, int limit) {
    checkLimit(limit);
    this.textField = target;
    this.limit = limit;
    cache = new ArrayList<String>(limit);
    target.addKeyListener(this);
    target.addActionListener(this);
  }

  /** Check that limit is not negative
   * @param limit Limit to check for */
  private void checkLimit(int limit) {
    if (limit < 3) throw new IllegalArgumentException("TextFieldCache size limit too small");
  }

  /**
   * Navigate cache on up/down
   * @param e KeyEvent
   */
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      // Go up in command queue 
      if (commandIndex >= cache.size()) return;
      if (newCommand) {
        String txt = textField.getText();
        commandIndex = pushCache(txt) ? 1 : 0; // Push current command into queue?
      } else if (textField.getText().length() > 0) commandIndex++; // Already queued command; navigage queue
      updateCurrentCommand();
      trimCache();
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      if (textField.getText().length() <= 0) {
        // TextField empty; just reset the cache
        commandIndex = 0;
        newCommand = false;
        return;
      }
      if (!newCommand) {
        if (commandIndex == 0) {
          // Last command; reset field
          textField.setText("");
        } else {
          // Queued command; navigate queye
          commandIndex--;
          updateCurrentCommand();
        }
      } else {
        // New command; push and reset field
        String txt = textField.getText();
        pushCache(txt);
        commandIndex = 0;
        textField.setText("");
        newCommand = false;
      }
    } else {
      // Other key usually means a new command is being composed 
      newCommand = true;
      commandIndex = 0;
    }
  }

  private void updateCurrentCommand() {textField.setText(cache.get(cache.size() - commandIndex - 1));}

  /**
   * Add cache on action
   * @param e KeyEvent
   */
  public void actionPerformed(ActionEvent e) {
    push(e.getActionCommand());
  }

  /**
   * Push current textfield's content into cache
   * @param cmd String to push
   */
  private void push(String cmd) {
    pushCache(cmd);
    commandIndex = 0;
  }

  /**
   * Really push command into cache, reset new command flag, check cache size, and nothing else
   * @param txt String to add
   * @return true if cache is updated; false if cache may be empty
   */
  private boolean pushCache(String txt) {
    newCommand = false;
    if (txt.length() == 0) return false;
    if (cache.size() > 0 && cache.get(cache.size()-1).equals(txt)) return true;
    cache.add(txt);
    trimCache();
    return true;
  }

  public JTextField getTextField() {
    return textField;
  }

  public void setTextField(JTextField textField) {
    this.textField.removeActionListener(this);
    this.textField.removeKeyListener(this);
    this.textField = textField;
    if (textField == null) return;
    textField.addActionListener(this);
    textField.addKeyListener(this);
    commandIndex = 0;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    checkLimit(limit);
    this.limit = limit;
    trimCache();
  }

  private void trimCache() {
    while (cache.size() > limit) cache.remove(0);
  }
}

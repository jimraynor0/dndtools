package org.sheepy.swing;

import javax.swing.Action;

/**
 * An exception indicates that required action cannot be found.
 * Created by Ho Yiu Yeung on 11-May-2005 at 02:47:01
 */
public class ActionNotFoundException extends RuntimeException {
  private Action action;

  public ActionNotFoundException() {
    super();
  }

  public ActionNotFoundException(String message, Action action) {
    super(message);
    this.action = action;
  }

  public Action getAction() {
    return action;
  }
}
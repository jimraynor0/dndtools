package org.sheepy.swing;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * An adapter class for MenuListener and PopupMenuListener.  Just override needed methods.
 * Created by Ho Yiu Yeung on 10-Nov-2005 at 17:42:22
 */
public class MenuAdapter implements MenuListener, PopupMenuListener {
  public void menuSelected(MenuEvent e) {}
  public void menuDeselected(MenuEvent e) {}
  public void menuCanceled(MenuEvent e) {}
  public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
  public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
  public void popupMenuCanceled(PopupMenuEvent e) {}
}

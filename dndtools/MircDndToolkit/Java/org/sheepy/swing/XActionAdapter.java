package org.sheepy.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.sheepy.util.StringUtil;

/**
 * An adapter for AbstractActions
 * Created by Ho Yiu Yeung on 14-Nov-2005 at 18:56:32 */
public class XActionAdapter extends JAction {
  Action action;

  public XActionAdapter(AbstractAction action) {
    this.action = action;
    for (Object obj : action.getKeys())
      putValue(obj.toString(), action.getValue(obj.toString()));
  }

  public XActionAdapter(AbstractAction action, String name, Icon icon, String hotkey, String hint) {
    this.action = action;
    for (Object obj : action.getKeys())
      putValue(obj.toString(), action.getValue(obj.toString()));
    if (!StringUtil.isEmpty(name)) setName(name);
    if (icon != null) setIcon(icon);
    if (!StringUtil.isEmpty(hotkey)) setHotKey(hotkey);
    if (!StringUtil.isEmpty(hint)) putValue(SHORT_DESCRIPTION, hint);
  }

  public void actionPerformed(ActionEvent e) {
    action.actionPerformed(e);
  }
}

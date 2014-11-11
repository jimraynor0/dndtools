package org.sheepy.swing;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

/**
 * A JAction that can be selected / unselected.
 * Create JCheckBoxMenuItem instead of JMenuItem.
 * Created by Ho Yiu Yeung on 12-Nov-2005 at 04:39:44 */
public class JToggleAction extends JAction implements XToggleAction {
  public JToggleAction() {super();}

  public JToggleAction(String name) {
    super(name);
  }

  public JToggleAction(String name, boolean selected) {
    super(name);
    if (selected) setSelected(selected);
  }

  /**
   * Create a JAction with given details
   * @param name Display name
   * @param icon Display icon
   * @param hotkey Hotkey as parsed by KeyEvent
   * @param hint Short describtion used as hint
   * @param selected Initial selected state
   */
  public JToggleAction(String name, Icon icon, String hotkey, String hint, boolean selected) {
    super(name, icon, hotkey, hint);
    if (selected) setSelected(selected);
  }

  /**
   * Called when the action is performed, or when the action is selected / deselected.
   * Toggle state by default
   * @param e
   */
  public void actionPerformed(ActionEvent e) {
    setSelected(!isSelected());
  };

  /** Creates a new checkbox meun item that is associated with this action. */
  public JMenuItem createMenuItem() {
    JMenuItem result = new JCheckBoxMenuItem(this);
    result.setSelected(isSelected());
/*    filteredResult.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
        setSelected(((JCheckBoxMenuItem)e.getSource()).isSelected());}});*/
    associateComponent(result);
    return result;
  }

  protected boolean updateSpecialComponent(JComponent comp) {
    if (comp instanceof JCheckBoxMenuItem) {
      updateComponent((JCheckBoxMenuItem)comp);
      return true;
    }
    return super.updateSpecialComponent(comp);
  }

  public void updateComponent(JCheckBoxMenuItem comp) {
    if (comp == null) return;
    updateJComponent((JComponent)comp);
    comp.setSelected(isSelected());
  }

  public void putValue(String key, Object newValue) {
    if (key.equals(SELECTED))
      setSelected((Boolean)newValue);
    else
      super.putValue(key, newValue);
  }

  public boolean isSelected() {
    Object o = getValue(SELECTED);
    return o != null && o instanceof Boolean && (Boolean)o;
  }

  public boolean setSelected(boolean newValue) {
    boolean oldValue = isSelected();
    if (oldValue != newValue) {
      super.putValue(SELECTED, newValue);
      firePropertyChange("selected", oldValue, newValue);
      updateComponents();
    }
    return oldValue;
  }
}

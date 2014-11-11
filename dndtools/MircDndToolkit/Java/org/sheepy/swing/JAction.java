package org.sheepy.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

/**
 * A basic implementation of XAction. Supports Menu item and quick button.
 * Created by Ho Yiu Yeung on 11-May-2005 at 02:53:26
 */
public abstract class JAction extends AbstractJAction {

  /** Constant for creating a menu component. @see #createComponent(Swing) */
  public static final String MENU_ITEM = "MENU_ITEM";
  /** Constant for creating a quick button component. @see #createComponent(Swing) */
  public static final String QUICK_BUTTON = "QUICK_BUTTON";

  public JAction() { super(); }

  public JAction(String name) { super(name); }

  /**
   * Create a JAction with given details
   * @param name Display name
   * @param icon Display icon
   * @param hotkey Hotkey as parsed by KeyEvent
   * @param hint Short describtion used as hint
   */
  public JAction(String name, Icon icon, String hotkey, String hint) { super(name, icon, hotkey, hint); }


  /**
   * Create and return an associated component.
   * Support menu and quick button.
   * @see #associateComponent(JComponent)
   * @param componentType Type of component to create.
   * @throws UnsupportedOperationException If the component class is not supported
   * @return created component
   */
  public JComponent createComponent(String componentType) throws UnsupportedOperationException {
    if (componentType == null) throw new UnsupportedOperationException();
    if (componentType.equals(MENU_ITEM)) return createMenuItem();
    else if (componentType.equals(QUICK_BUTTON)) return createQuickButton();
    else return super.createComponent(componentType);
  }

  /** Creates a new meun item that is associated with this action. 
   * @return Associated MenuItem */
  protected JMenuItem createMenuItem() {
    JMenuItem result = new JMenuItem(this);
    associateComponent(result);
    return result;
  }

  /** Creates a new quick button that is associated with this action. 
   * @return Associated JButton with no text */
  protected JButton createQuickButton() {
    JButton result = new JButton(this);
    if (getIcon() != null) result.putClientProperty("hideActionText", true);
    associateComponent(result);
    return result;
  }

  public void unassociateComponent(JComponent comp) {
    if (comp == null) return;
    if (comp instanceof AbstractButton) ((AbstractButton)comp).setAction(null);
    super.unassociateComponent(comp);
  }

  /**
   * Check for AbstractButton to set the action
   * Return true if the comopnent is known and is updated. Return false otherwise.
   * @param comp
   */
  protected boolean updateSpecialComponent(JComponent comp) {
    if (comp instanceof AbstractButton) {
      updateComponent((AbstractButton)comp);
      return true;
    }
    return false;
  }

  /** Update given component's action - name, icon, mnemonic, hint, etc.
   * @param comp */
  public void updateComponent(AbstractButton comp) {
    if (comp == null) return;
    updateJComponent((JComponent)comp);
    comp.setAction(this);
  }

  /**
   * A JAction that cannot be enabled in anyway.
   * Suitable for error message that serves as placeholder (like "cannot load menu").
   */
  public static class JDisabledAction extends JAction {
    public JDisabledAction(String name) {
      super(name);
      setEnabled(false);
    }

    public void setEnabled(boolean newValue) { super.setEnabled(false); }
    public void actionPerformed(ActionEvent e) {}
  }
}

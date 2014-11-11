package org.sheepy.swing;

import java.util.WeakHashMap;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.sheepy.util.SwingUtil;

/**
 * An abstract inplementation of JAction that does not support any particular components.
 * Associated components are stored with weak reference so may be recycled if there are no other references.
 * update is a good method to override for actions that wants to be refreshed easily with other actions.
 * Created by Ho Yiu Yeung on 04-Jul-2005 at 23:45:03
 */
public abstract class AbstractJAction extends AbstractAction implements XAction, Comparable<XAction> {
  // The object value is not used.  It's just for the weak hash map
  protected WeakHashMap<JComponent, Object> components;

  public AbstractJAction() {
    super();
  }

  public AbstractJAction(String name) {
    super();
    if (name != null) setName(name);
  }

  /**
   * Create an JAction with specified parameters.
   * @param name Text (and mnemonic) of the action.  See setName for details.
   * @param icon Icon of the action.
   * @param hotkey Hotkey for the action.  See setHotKey for details.
   * @param hint Hint of the action.
   */
  public AbstractJAction(String name, Icon icon, String hotkey, String hint) {
    super();
    setAll(name, icon, hotkey, hint);
  }

  protected void setAll(String name, Icon icon, String hotkey, String hint) {
    if (name != null) setName(name);
    if (icon != null) putValue(SMALL_ICON, icon);
    if (hint != null) putValue(SHORT_DESCRIPTION, hint); else if (name != null) putValue(SHORT_DESCRIPTION, name);
    if (hotkey != null) setHotKey(hotkey);
  }

  /**
   * No JComopnent supported.  Throws UnsupportedOperationException.
   * @param componentType
   * @return Created JComponent
   * @throws UnsupportedOperationException
   */
  public JComponent createComponent(String componentType) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  };

  public void associateComponent(JComponent comp) throws UnsupportedOperationException {
    if (comp == null) return;
    if (components != null && components.containsKey(comp)) return;
    if (addComponent(comp))
      updateComponent(comp);
  }

  public void unassociateComponent(JComponent comp) {
    if (components == null) return;
    components.remove(comp);
    synchronized(this) {
      if (components.size() <= 0) components = null;
    }
  }

  /** Add given components to internal list.
   * Return true if it is added, false if already exist and is not added. 
   * @param comp Component to add
   * @return true if added, false if already in list */
  protected boolean addComponent(JComponent comp) {
    if (components == null)
      synchronized(this) {
        if (components == null) components = new WeakHashMap<JComponent, Object>();
        else if (components.containsKey(comp)) return false;
      }
    components.put(comp, null);
    return true;
  }

  public Object[] componentList() {
    if (components == null) return new Object[0];
    return components.keySet().toArray();
  }

  public void update() {}

  /**
   * Set name and mnemonic of the action.
   * @param name Name of the action marked with mnemonic.
   * @see org.sheepy.util.SwingUtil#parseName(StringBuffer)
   */
  public void setName(String name) {
    if (name == null) {
      uncheckedPutValue(NAME, null);
      uncheckedPutValue(MNEMONIC_KEY, null);
    } else {
      StringBuffer str = new StringBuffer(name);
      int m = SwingUtil.parseName(str);
      if (str.length() > 0) {
        uncheckedPutValue(NAME, str.toString());
        uncheckedPutValue(MNEMONIC_KEY, (m > 0)?m:null);
      } else {
        uncheckedPutValue(NAME, null);
        uncheckedPutValue(MNEMONIC_KEY, null);
      }
    }
    updateComponents();
  }

  /**
   * Return name of this action
   * @return Name of the action
   */
  public String getName() {
    return (String)getValue(NAME);
  }

  /**
   * Set hotkey of the action.
   * Hot key must be provided in form of KeyStrok syntax like "ctrl alt ESC", case sensitive.
   * @param hotkey Hoykey to be set in string form.
   */
  public void setHotKey(String hotkey) {
    if (hotkey == null) {
      setHotKey((KeyStroke)null);
      return;
    } else if (hotkey.length() <= 1)  // Single-key mnemonic
      hotkey = hotkey.toUpperCase();
    KeyStroke k = KeyStroke.getKeyStroke(hotkey);
    if (k != null) setHotKey(k);
  }

  /**
   * Set hotkey of the action.
   * @param hotkey Hoykey to be set.
   */
  public void setHotKey(KeyStroke hotkey) {
    if (getValue(ACCELERATOR_KEY) == hotkey) return;
    uncheckedPutValue(ACCELERATOR_KEY, hotkey);
    updateComponents();
  }

  /**
   * Get hot key in String in KeyStoke toString() form.
   * Example: ctrl pressed S
   * @return hot key in String in KeyStoke toString() form
   */
  public KeyStroke getHotkey() {
    return (KeyStroke)getValue(ACCELERATOR_KEY);
  }

  /**
   * Put a value without any check.
   * @param key
   * @param newValue
   */
  protected void uncheckedPutValue(String key, Object newValue) {
    try {
      super.putValue(key, newValue);
    } catch (NullPointerException e) {
      if (newValue != null) throw e; // Discard NPE caused by Swing's unchecked PropertyChange handlers
    }
  }

  public XActionList getParent() {
    return (XActionList)getValue(PARENT);
  }

  public void setParent(XActionList parent) {
    uncheckedPutValue(PARENT, parent);
    if (!parent.contains(this)) parent.add(this);
  }

  public boolean isVisible() {
    Object state = getValue(VISIBLE);
    if (state != null)
      if (state instanceof Boolean)
        return (Boolean)state;
      else
        return true;
    else
      return true;
  }

  public void setVisible(boolean visible) {
    Boolean old = isVisible();
    putValue(VISIBLE, visible);
    firePropertyChange("visible", old, visible);
  }

  public void setIcon(Icon icon) {
    putValue(SMALL_ICON, icon);
  }

  public Icon getIcon() {
    Object icon = getValue(SMALL_ICON);
    if (icon instanceof Icon)
      return (Icon)icon;
    else
      return null;
  }

  public Object getValue(String key) {
    if (key == ENABLED) return isEnabled();
    return super.getValue(key);
  }

  public void putValue(String key, Object newValue) {
    if (key == null) return;
    if (key.equals(ENABLED)) {
      setEnabled((Boolean)newValue);
      return;
    }
    if (newValue == getValue(key) || (newValue != null && newValue.equals(getValue(key)))) return;
    if (key.equals(NAME))
      setName(newValue.toString());
    else if (key.equals(ACCELERATOR_KEY) && (newValue != null)) {
      setHotKey(newValue.toString());
      return;
    } else if (key.equals(PARENT))
      setParent((XActionList)newValue);
    else
      super.putValue(key, newValue);
    if (key.equals(VISIBLE))
      updateComponents();
  }

  /** Update given components's properties 
   * @param comp Component to update */
  public void updateComponent(JComponent comp) {
    if (comp == null) return;
    if (!updateSpecialComponent(comp)) updateJComponent(comp);
  }

  /** Do an update on all associated components */
  public void updateAllComponent() {
    if (components == null) return;
    for (JComponent c : components.keySet())
      this.updateComponent(c);
  }

  /** Update a JComponent's name, visibility and availability 
   * @param comp Component to update */
  protected void updateJComponent(JComponent comp) {
    if (comp == null) return;
    comp.setName(getName());
    comp.setVisible(isVisible());
    comp.setEnabled(isEnabled());
  }

  /**
   * Check for components of special type to update. Subclass may ovwerride this as they support more components
   * Return true if the comopnent is known and is updated. Return false will update it as generic JComponent.
   * @param comp Component to update
   * @return true if updated, false if normal update is needed
   */
  protected boolean updateSpecialComponent(JComponent comp) { return false; }

  /** Update state of all components. */
  protected void updateComponents() {
    if (components == null) return;
    for (JComponent c : components.keySet())
      updateComponent(c);
  }

  public int compareTo(XAction xAction) {
    if (xAction == null) return 1;
    String name = getName();
    if (name == null) return (xAction.getValue(NAME) == null) ? 0 : -1;
    return name.compareTo(xAction.getValue(NAME).toString());
  }
}

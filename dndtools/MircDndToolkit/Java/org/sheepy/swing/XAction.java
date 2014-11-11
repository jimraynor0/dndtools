package org.sheepy.swing;

import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Extended Action.  Contains methods
 * Created by Ho Yiu Yeung on 10-May-2005 at 20:40:13
 */
public interface XAction extends Action {
  /** The key used for storing the enabled status (Boolean) of the action.
   * Non-existant implies enabled.
   * @see #putValue(String, Object)
   * @see #getValue(String)
   */
  public static final String ENABLED = "ENABLED";

  /** The key used for storing the visible status (Boolean) of the action.
   * Non-existant implies visible.
   * @see #putValue(String, Object)
   * @see #getValue(String)
   */
  public static final String VISIBLE = "VISIBLE";

  /** The key used for storing the parent (XActionList) of the action.
   * @see #putValue(String, Object)
   * @see #getValue(String)
   */
  public static final String PARENT = "PARENT";

  /**
   * Create and return an associated component.
   * Each call to this method returns a new component.
   * Different XAction implementation supports different components.
   * Please check documentation before creating
   * @see #associateComponent(JComponent)
   * @param componentType Type of component to create.
   * @throws UnsupportedOperationException If the component class is not supported
   * @return created component
   */
  public JComponent createComponent(String componentType) throws UnsupportedOperationException;

  /**
   * Set properties of given component and add listeners to it to associate with it.
   * If already associated with given component, do nothing.
   *
   * Once a component is associated with this action, state change of this action will affect the component,
   * and some value change (varies with implementation) may affect the state of the action.
   * 
   * @param comp Component to associate
   * @throws UnsupportedOperationException if given component is not supported.
   * @see #unassociateComponent(JComponent)
   */
  public void associateComponent(JComponent comp) throws UnsupportedOperationException;

  /**
   * Unassociate this action from given component.
   * The action will no longer affects the component.
   * Do nothing if there is no association.
   * @see  #associateComponent(JComponent)
   * @param comp
   */
  public void unassociateComponent(JComponent comp);

  /**
   * Get list of associated component of this action.
   * @return A list of components
   */
  public Object[] componentList();

  /**
   * Given the action a chance to update its status.
   * Should be called when component of the action is to be displayed, e.g. when menu pops up.
   */
  public void update();
}

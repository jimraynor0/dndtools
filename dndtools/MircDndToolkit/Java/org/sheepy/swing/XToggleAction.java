package org.sheepy.swing;

/**
 * An action with two state - selected (on) or not selected (off)
 *  Created by Ho Yiu Yeung on 12-Nov-2005 at 04:36:17 */
public interface XToggleAction extends XAction {
  /**
   * The key used to store selected status of the action
   */
  public final String SELECTED = "SELECTED";

  /**
   * Return true if the action is selected.
   * @return selected state
   */
  public boolean isSelected();

  /**
   * Set selected state of the action.
   * @param newValue new state
   * @return Original selected state of the action. 
   */
  public boolean setSelected(boolean newValue);
}

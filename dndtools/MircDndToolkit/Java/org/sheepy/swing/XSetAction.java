package org.sheepy.swing;

import java.util.Map;

/**
 * A set of XAction combined to act as a single XAction.
 * Created by Ho Yiu Yeung on 11-May-2005 at 01:37:01
 */
public interface XSetAction extends XAction, Map<Object, XAction> {
  /** The key used for storing a map of XActions (Map<XAction>)
   * @see #putValue(String, Object)
   * @see #getValue(String)
   */
  public final String SETS = "SETS";

  /**
   * Change currentMod action to given one, if it is in the set.
   * @param action Action to change to
   * @throws ActionNotFoundException If requested action is not found in the set.
   */
  public void changeAction(XAction action) throws ActionNotFoundException;

  /**
   * Change currentMod action to one with given key, if it is in the set.
   * @param key Key of action to change to
   * @throws ActionNotFoundException If requested key (and thus its action) is not found in the set.
   */
  public void changeAction(Object key) throws ActionNotFoundException;
}

package org.sheepy.swing;

import java.util.List;

/**
 * A list of XAction.  The createComponent method of this action should
 * create and return a compound component that makes up of associated components of actions in the list.
 *
 * Also it must maintain children's parent when they are being added / removed.
 *
 * All operations should apply to associated components of this list only.   
 * Created by Ho Yiu Yeung on 10-May-2005 at 22:05:33
 */
public interface XActionList extends XAction, List<XAction> {
}

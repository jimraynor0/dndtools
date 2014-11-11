package org.sheepy.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.PopupMenuEvent;

/**
 * A basic implementation of XActionList. Supports JMenu and JToolbar.
 * Created by Ho Yiu Yeung on 04-Jul-2005 at 23:55:07
 */
public class JActionList extends AbstractJAction implements XActionList {

  public static final String MENU = "MENU";
  public static final String MENUBAR = "MENUBAR";
  public static final String TOOLBAR = "TOOLBAR";
  /** Constant for creating a quick button component. @see #createComponent(Swing) */
  public static final String QUICK_BUTTON = "QUICK_BUTTON";

  public static final XAction SEPARATOR = new AbstractJAction() {
    public void actionPerformed(ActionEvent e) {}};

  public JActionList() { super(); }
  public JActionList(String name) { super(name); }
  public JActionList(String name, Icon icon, String hint) { super(name, icon, null, hint); }
  public JActionList(String name, XAction[] actions) { super(name); add(actions); }
  public JActionList(String name, Icon icon, String hint, XAction[] actions) { super(name, icon, null, hint); add(actions); }

  public void actionPerformed(ActionEvent e) {}

  protected final ArrayList<XAction> actions = new ArrayList<XAction>(10);

  public JComponent createComponent(String componentType) throws UnsupportedOperationException {
    if (componentType == null) throw new UnsupportedOperationException();
    if (componentType.equals(MENU)) return createMenu();
    else if (componentType.equals(MENUBAR)) return createMenuBar();
    else if (componentType.equals(TOOLBAR)) return createToolBar();
    else if (componentType.equals(QUICK_BUTTON)) return createQuickButton();
    else return super.createComponent(componentType);
  }

  /** Create a new toolbar that will be filled up by child actions. 
   * @return Associated JToolBar */
  protected JToolBar createToolBar() {
    JToolBar result = new JToolBar(getName());
    for (XAction act : actions)
      if (act == SEPARATOR)
        result.addSeparator();
      else if (act instanceof XActionList)
        result.add(act.createComponent(TOOLBAR));
      else
        result.add(act);
    this.addComponent(result);
    this.updateComponent(result);
    return result;
  }

  /** Creates a new quick button that is associated with this action. 
   * @return Associated quick button without that popup menu onAction */
  protected JButton createQuickButton() {
    final JButton result = new JButton(this);
    if (getIcon() != null) {
      result.putClientProperty("hideActionText", true);
      result.setText("");
    }
    associateComponent(result);
    result.addActionListener(new ActionListener() {
      JPopupMenu menu;
      public void actionPerformed(ActionEvent e) {
        if (menu == null) menu = createPopupMenu();
        menu.show(result, (int)0, (int)result.getHeight());
      }});
    return result;
  }

  /** Create a new menu that will be filled up by child actions. 
   * @return Associated menu */
  protected JMenu createMenu() {
    JMenu result = new JMenu(this);
    for (XAction act : actions)
      if (act == SEPARATOR)
        result.addSeparator();
      else if (act instanceof XActionList)
        result.add(act.createComponent(MENU));
      else if (act instanceof JAction)
        result.add(act.createComponent(JAction.MENU_ITEM));
      else
        result.add(act);
    this.addComponent(result);
    this.updateComponent(result);
    result.addMenuListener(new MenuAdapter(){
      public void menuSelected(MenuEvent e) {update();}
    });
    return result;
  }

  /** Create a new popup menu that will be filled up by child actions. 
   * @return Create associated popup menu */
  protected JPopupMenu createPopupMenu() {
    JPopupMenu result = new JPopupMenu(getName());
    for (XAction act : actions)
      if (act == SEPARATOR)
        result.addSeparator();
      else if (act instanceof XActionList)
        result.add(act.createComponent(MENU));
      else if (act instanceof JAction)
        result.add(act.createComponent(JAction.MENU_ITEM));
      else
        result.add(act);
    this.addComponent(result);
    this.updateComponent(result);
    result.addPopupMenuListener(new MenuAdapter(){
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {update();}
    });
    return result;
  }

  /** Create a new menu bar that will be filled up by child actions. 
   * @return Create associated menubar */
  protected JMenuBar createMenuBar() {
    JMenuBar result = new JMenuBar();
    for (XAction act : actions)
      if (act instanceof XActionList)
        result.add(act.createComponent(MENU));
      else {
        JMenu menu = new JMenu(act);
        menu.add(act.createComponent(JAction.MENU_ITEM));
        result.add(menu);
      }
    this.addComponent(result);
    this.updateComponent(result);
    return result;
  }

  public void update() {
    for (XAction act : actions)
      act.update();
    /* TODO: synchronise toolbar and menus */
  }

  protected boolean updateSpecialComponent(JComponent comp) {
    if (comp instanceof JButton) {
      updateJComponent(comp);
      if (getIcon() != null) ((JButton)comp).setText("");
      return true;
    } else
      return super.updateSpecialComponent(comp);    //To change body of overridden methods use File | Settings | File Templates.
  }

  /***************** List wrapper *****************************/

  public int size() {
    return actions.size();
  }

  public boolean isEmpty() {
    return actions.isEmpty();
  }

  public boolean contains(Object o) {
    return actions.contains(o);
  }

  public Iterator<XAction> iterator() {
    return actions.iterator();
  }

  public Object[] toArray() {
    return actions.toArray();
  }

  public <T> T[] toArray(T[] ts) {
    return actions.toArray(ts);
  }

  public boolean add(XAction xAction) {
    return actions.add(xAction);
  }

  public void add(XAction[] xAction) {
    for (XAction action : xAction)
      actions.add(action);
  }

  public boolean remove(Object o) {
    return actions.remove(o);
  }

  public boolean containsAll(Collection<?> c) {
    return actions.containsAll(c);
  }

  public boolean addAll(Collection<? extends XAction> xActions) {
    return actions.addAll(xActions);
  }

  public boolean addAll(int index, Collection<? extends XAction> xActions) {
    return actions.addAll(index, xActions);
  }

  public boolean removeAll(Collection<?> c) {
    return actions.removeAll(c);
  }

  public boolean retainAll(Collection<?> c) {
    return actions.retainAll(c);
  }

  public void clear() {
    actions.clear();
  }

  public XAction get(int index) {
    return actions.get(index);
  }

  public XAction set(int index, XAction xAction) {
    return actions.set(index, xAction);
  }

  public void add(int index, XAction xAction) {
    actions.add(index, xAction);
  }

  public XAction remove(int index) {
    return actions.remove(index);
  }

  public int indexOf(Object o) {
    return actions.indexOf(o);
  }

  public int lastIndexOf(Object o) {
    return actions.lastIndexOf(o);
  }

  public ListIterator<XAction> listIterator() {
    return actions.listIterator();
  }

  public ListIterator<XAction> listIterator(int index) {
    return actions.listIterator(index);
  }

  public List<XAction> subList(int fromIndex, int toIndex) {
    return actions.subList(fromIndex, toIndex);
  }
}

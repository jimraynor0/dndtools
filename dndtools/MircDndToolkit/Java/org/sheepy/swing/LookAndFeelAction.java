package org.sheepy.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.sheepy.data.FileResources;

/** Look & feel changing actions.
 * @author Ho Yiu Yeung
 */
public class LookAndFeelAction extends JAction {
  UIManager.LookAndFeelInfo lnf;
  Component root;

  /**
   * Create an action that changes the look and feel of given root component.
   * Look and feel image is loaded from res, in form of <code>"skin_" + first 3 character of L&F + ".gif"</code>,
   * e.g. "skin_cde.gif", "skin_win.gif".
   * @param info
   * @param res
   * @param root
   */
  public LookAndFeelAction(UIManager.LookAndFeelInfo info, FileResources res, Component root) {
    super(info.getName());
    if (res != null)
    putValue(SMALL_ICON, res.loadImageIcon("skin_"+info.getName().substring(0,3)+".gif"));
    putValue(SHORT_DESCRIPTION, "Apply skin: "+info.getName());
    putValue(MNEMONIC_KEY, new Integer(info.getName().charAt(0)));
    putValue(ACCELERATOR_KEY, null);
    lnf = info;
    this.root = root;
  }

  /** Test for and create a LookAndFeelAction 
   * @param name Name of LnF
   * @param className Classname of LnF
   * @param res Image resource folder
   * @param root Root GUI component
   * @param menu LnF action set */
  private static void testAndCreate(String name, String className, FileResources res, Component root, JActionList menu)  {
    try {
      Class.forName(className);
      LookAndFeelAction a = new LookAndFeelAction(new UIManager.LookAndFeelInfo(name, className), res, root);
      if (a != null) menu.actions.add(a);
    } catch (ClassNotFoundException e) {
      return;
    }
  }

  public void actionPerformed(ActionEvent e) {
    try {
      UIManager.setLookAndFeel(lnf.getClassName());
      SwingUtilities.updateComponentTreeUI(root);
    } catch (Exception x) { x.printStackTrace(); }
  }

  /**
   * Create a look & feel menu.
   * @param res Resource folder to look for images
   * @param root Root component to update skin
   * @return Dymanically created menu will all known look & feel listed (execpt classic window)
   */
  public static JActionList createLookAndFeelActions(FileResources res, Component root) {
    UIManager.LookAndFeelInfo [] installedLnF = UIManager.getInstalledLookAndFeels();;
    JActionList lnf = new JActionList("Skin");
    for (UIManager.LookAndFeelInfo info : installedLnF)
      if (!info.getName().equals("Windows Classic"))
        lnf.actions.add(new LookAndFeelAction(info, res, root ));

    testAndCreate("Kunststoff", "com.incors.plaf.kunststoff.KunststoffLookAndFeel", res, root, lnf);
    testAndCreate("Napkin", "napkin.NapkinLookAndFeel", res, root, lnf);
    testAndCreate("Oyoaha", "com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel", res, root, lnf);
    return lnf;
  }

}

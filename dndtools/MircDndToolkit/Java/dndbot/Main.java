package dndbot;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TrayIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jibble.pircbot.Colors;
import org.sheepy.util.SwingUtil;

import res.img.ImagePath;
import dndbot.irc.IRC;
import dndbot.ui.PnlDiceBot;

/**
 * DnDBot main class.  Create a form containing DnDBot GUI and that's it.
 *
 * Created by Ho Yiu YEUNG on Mar 8, 2007
 */
public class Main {

  Preferences pref = Preferences.userNodeForPackage(getClass());

  private static Boolean minToTray = null;
  private static Boolean autoConn = null;
  private static Boolean noLog = null;
  private static int delay = 0;
  private static TrayIcon icon;

  public static final void main(String[] args) {
    for (String s : args) {
      if (s.startsWith("-") || s.startsWith("/")) {
        String param = s.substring(1).toLowerCase();
        if (param.equals("min")) minToTray = true;
        else if (param.equals("autoconn")) autoConn = true;
        else if (param.equals("nolog")) noLog = true;
        else if (param.equals("log")) noLog = false;
        else if (param.startsWith("delay=")) delay = Integer.parseInt(param.substring(6));
        else System.err.println("Unknown parameter: "+param);
      } else {
        System.out.println(args);
      }
    }
    if (delay > 0)
		try {
			Thread.sleep(delay * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    new Main().run();
    /*
    String exp = "d";
    Matcher matcher = multiplierPattern.matcher(exp);
    System.out.println(matcher.matches());
    if (matcher.matches())
      for (int i = 0; i < matcher.groupCount(); i++)
        System.out.println(matcher.group(i));
        */
  }

  public void run() {
//    /*
    final PnlDiceBot pBot = new PnlDiceBot();
    final JFrame frmMain = new JFrame(Colors.removeFormattingAndColors(IRC.version));
    frmMain.setLayout(new BorderLayout());
    frmMain.add(pBot);
    if (noLog != null) pBot.setMonitor(!noLog);
    pBot.refreshTitle();

    WindowAdapter frmMainListener = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        frmMain.setState(JFrame.NORMAL); // Restore to normal size before saving size
        frmMain.dispose();
      }
      public void windowClosed(WindowEvent e) {
        try {
          // Save size
          pref.putInt("frame_width", frmMain.getWidth());
          pref.putInt("frame_height", frmMain.getHeight());
          frmMain.setVisible(false);
          // Stop bot
          pBot.stopBot();
          // Wait for disconnect.  Just in case.
          Thread.sleep(500);
        } catch (Exception e1) {
        } finally { System.exit(0); }
      }
      public void windowGainedFocus(WindowEvent e) {
        pBot.grabFocus(); // Make sure PblDiceBot got the focus so it'll refreshLog
      }
    };
    frmMain.addWindowListener(frmMainListener);
    frmMain.addWindowFocusListener(frmMainListener);
    // Load size from last run
    int width = pref.getInt("frame_width", 350);
    int height = pref.getInt("frame_height", 300);
    frmMain.setSize(width, height);
    SwingUtil.centreFrame(frmMain);

    // Try to setup tray icon
    try {
//      PopupMenu pop = new PopupMenu();
//      pop.add(new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X)));
      frmMain.setIconImage(ImagePath.loadIcon("tray.jpg").getImage());
      icon = SwingUtil.setMinimiseTray(frmMain, null, null);
      if (icon != null)
        System.err.println("JRE version less then 6, tray icon not supported.");
    } catch (Exception e) {}

    SwingUtilities.invokeLater(new Runnable() { public void run() {
      frmMain.setVisible(true);
      try {
        Thread.sleep(500); // Wait so that splash screen can detect frame and go away. Otherwise it will stay visible for a while.
      } catch (InterruptedException e) {
      }
      if (minToTray != null && minToTray) frmMain.setState(Frame.ICONIFIED);
      if (autoConn != null && autoConn) pBot.startBot();
      }});
    /**/
  }

}

/*
 * Created on 2004/3/2
 */
package org.sheepy.util;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataListener;

/**
 * Swing utilities.
 *
 * @author Ho Yiu Yeung
 */
public class SwingUtil {
  /**
   * Add internal raised bevel. Incompatible with other internal border style.
   */
  public static final int BORDER_RAISED = 0x100;
  /**
   * Add internal lowered bevel.  Incompatible with other internal border style.
   */
  public static final int BORDER_LOWERED = 0x200;
  /**
   * Add internal lowered etch.  Incompatible with other internal border style.
   */
  public static final int BORDER_ETCHED = 0x300;
  /**
   * Add internal raised etch.  Incompatible with other internal border style.
   */
  public static final int BORDER_FRAMED = 0x400;

  /**
   * Add lowered title.  Incompatible with other title border style.
   */
  public static final int TITLE_LOWERED = 0x1000;
  /**
   * Add raised title. Incompatible with other title border style.
   */
  public static final int TITLE_RAISED = 0x2000;
  /**
   * Add etched title.  Incompatible with other title border style.
   */
  public static final int TITLE_ETCHED = 0x3000;
  /**
   * Add frame title.  Incompatible with other title border style.
   */
  public static final int TITLE_FRAMED = 0x4000;
  /**
   * Add empty title.  Incompatible with other title border style.
   */
  public static final int TITLE_EMPTY = 0x8000;

  /**
   * Add external empty border of width 3, can combine with other external border style.
   */
  public static final int BORDER_EMPTY3 = 0x10000;
  /**
   * Add external empty border of width 5, can combine with other external border style.
   */
  public static final int BORDER_EMPTY5 = 0x20000;

  /**
   * Place child in JScrollPane and enable horizontal scroll as needed.
   */
  public static final int SCROLL_HORIZONTAL = 0x10;
  /**
   * Place child in JScrollPane and enable vertical scroll as needed.
   */
  public static final int SCROLL_VERTICAL = 0x20;
  /**
   * Place child in JScrollPane and enable horizontal and vertical scroll.
   */
  public static final int SCROLL_BOTH = SCROLL_HORIZONTAL | SCROLL_VERTICAL;
  /**
   * Show scroll bar(s) as needed (default).  Must combine with SCROLL_HORIZONTAL, SCROLL_VERTICAL, or SCROLL_BOTH.
   */
  public static final int SCROLL_AS_NEEDED = 0;
  /**
   * Always showMap scroll bar(s). Must combine with SCROLL_HORIZONTAL, SCROLL_VERTICAL, or SCROLL_BOTH.
   */
  public static final int SCROLL_ALWAYS = 1;

  /**
   * Add a border layout to the component.
   */
  public static final int LAYOUT_BORDER = 0x100000;
  /**
   * Add a card layout to the component.
   */
  public static final int LAYOUT_CARD = 0x200000;

  public static final int SCROLL_ON_DEMAND = SCROLL_BOTH | SCROLL_AS_NEEDED | BORDER_LOWERED;
  public static final int TITLE_SCROLL_ON_DEMAND = TITLE_ETCHED | SCROLL_ON_DEMAND;

  /**
   * Calculate width and height of insets.
   *
   * @param ins Insets to measure size of.
   * @return Combined size of borders.
   */
  public static Point InsetSize(Insets ins) {
    if (ins == null) return new Point(0, 0);
    return new Point(ins.right + ins.left, ins.bottom + ins.top);
  }


  /**
   * Horizontal scroll bar policy constants
   */
  private static final int[] HScrollStyle = {HORIZONTAL_SCROLLBAR_AS_NEEDED,
                                             HORIZONTAL_SCROLLBAR_ALWAYS,
                                             HORIZONTAL_SCROLLBAR_NEVER};
  /**
   * Vertical scroll bar policy constants
   */
  private static final int[] VScrollStyle = {VERTICAL_SCROLLBAR_AS_NEEDED,
                                             VERTICAL_SCROLLBAR_ALWAYS,
                                             VERTICAL_SCROLLBAR_NEVER};
  /**
   * Internal border mask
   */
  private static final int IntBorder = BORDER_RAISED | BORDER_LOWERED |
          BORDER_ETCHED | BORDER_FRAMED;
  /**
   * Title border mask
   */
  private static final int TitBorder = TITLE_RAISED | TITLE_LOWERED |
          TITLE_ETCHED | TITLE_FRAMED | TITLE_EMPTY;
  /**
   * External border mask
   */
  private static final int ExtBorder = BORDER_EMPTY3 | BORDER_EMPTY5;
  /**
   * Border mask
   */
  private static final int Borders = IntBorder | TitBorder | ExtBorder;

  /**
   * Warp child with panel and border of given style, and a title border.
   * Empty border always go first, then title, then internal border.
   * A scroll panel will be created if any scroll style is specified.
   * The Scroll panel goes deepest and by default showMap scrollbar(s) on need.
   *
   * @param child child component to warp.
   * @param style combination of panel and border styles.
   * @param title Title of title border.
   * @return Warped component to be displayed
   */
  public static JComponent decorateComp(JComponent child, int style, String title) {
    JComponent result = child;
    // Make scroll panel
    if ((style & SCROLL_BOTH) > 0) {
      boolean h = (style & SCROLL_HORIZONTAL) > 0;
      boolean v = (style & SCROLL_VERTICAL) > 0;
      int hs = style & SCROLL_ALWAYS;
      int vs = VScrollStyle[(v) ? hs : 2];
      hs = HScrollStyle[(h) ? hs : 2];
      JScrollPane p = new JScrollPane(result, vs, hs);
      p.setPreferredSize(result.getPreferredSize());
      result = p;
    }
    // Assign borders
    if ((result != null) && ((style & Borders) > 0)) {
      Border Ext = null;
      Border Tit = null;
      Border Int = null;
      switch (style & IntBorder) {
        case BORDER_RAISED:
          Int = BorderFactory.createRaisedBevelBorder();
          break;
        case BORDER_LOWERED:
          Int = BorderFactory.createLoweredBevelBorder();
          break;
        case BORDER_ETCHED:
          Int = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
          break;
        case BORDER_FRAMED:
          Int = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
          break;
      }
      if (title != null) {
        switch (style & TitBorder) {
          case TITLE_RAISED:
            Tit = BorderFactory.createRaisedBevelBorder();
            break;
          case TITLE_LOWERED:
            Tit = BorderFactory.createLoweredBevelBorder();
            break;
          case TITLE_ETCHED:
            Tit = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            break;
          case TITLE_FRAMED:
            Tit = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
            break;
          case TITLE_EMPTY:
            Tit = BorderFactory.createEmptyBorder();
            break;
        }
        if (Tit != null)
          Tit = BorderFactory.createTitledBorder(Tit, title);
        else
          Tit = BorderFactory.createTitledBorder(title);
      }
      switch (style & ExtBorder) {
        case BORDER_EMPTY3:
          Ext = BorderFactory.createEmptyBorder(3, 3, 3, 3);
          break;
        case BORDER_EMPTY5:
          Ext = BorderFactory.createEmptyBorder(5, 5, 5, 5);
          break;
        case BORDER_EMPTY3 | BORDER_EMPTY5:
          Ext = BorderFactory.createEmptyBorder(8, 8, 8, 8);
          break;
      }
      result.setBorder(createCompoundBorder(Ext, Tit, Int));
    }
    if ((style & LAYOUT_BORDER) > 0)
      result.setLayout(new BorderLayout());
    else if ((style & LAYOUT_CARD) > 0) result.setLayout(new CardLayout());
    return result;
  }

  /**
   * Warp child with panel and border of given style.
   *
   * @param child child component to warp.
   * @param style combination of panel and border styles.
   * @return Warped component to be displayed
   * @see SwingUtil#decorateComp(JComponent, int, String)
   */
  public static JComponent decorateComp(JComponent child, int style) {
    return decorateComp(child, style, null);
  }

  /**
   * Optimized compound border generator.
   *
   * @param out Outside border
   * @param mid Middle border
   * @param in  Internal border
   * @return Compound borderthat is combinatin of three borders.
   */
  public static Border createCompoundBorder(Border out, Border mid, Border in) {
    // Single border
    if ((in == null) && (mid == null))
      return out;
    else if ((out == null) && (mid == null))
      return in;
    else if ((in == null) && (out == null)) return mid;
    // Double borders
    if (out == null)
      return BorderFactory.createCompoundBorder(mid, in);
    else if (mid == null)
      return BorderFactory.createCompoundBorder(out, in);
    else if (in == null)
      return BorderFactory.createCompoundBorder(out, mid);
    // Triple borders
    return BorderFactory.createCompoundBorder(out,
            BorderFactory.createCompoundBorder(mid, in));
  }

  /**
   * Create a scroll always scrollpane containing given component.
   * @param comp Containing component
   * @return ScrollPanel with given component
   */
  public static JComponent scrollBoxAlways(JComponent comp) {
    return decorateComp(comp, SCROLL_BOTH + SCROLL_ALWAYS + BORDER_LOWERED, null);
  }

  /**
   * Create a scroll always scrollpane containing given component.
   * @param comp Containing component
   * @return ScrollPanel with given component
   */
  public static JComponent scrollBoxOnDemand(JComponent comp) {
    return decorateComp(comp, SCROLL_BOTH + SCROLL_AS_NEEDED + BORDER_LOWERED, null);
  }

  /**
   * Convert a KeyStroke to a user friendly string representation.
   * Example Ctrl+Alt+Esc
   * @param key KeyStroke to parse
   * @return User friendly string reprentation of the KeyStroke, or null if it is null.
   *//*
  public String easyHotKey(KeyStroke key) {
    if (key == null) return null;
    return key.toString().replaceAll("\\s", "+").
            replaceAll("ctrl","Ctrl").replaceAll("alt","Alt").replaceAll("meta","Meta").replaceAll("shift","Shift");
  }

  /**
   * Parse a user friendly keystroke representation into a real keystroke.
   * Example Ctrl+Alt+Esc
   * @param key KeyStroke to parse
   * @return
   *//*
  public KeyStroke parseEasyHotKey(String key) {
    if (key == null) return null;
    return KeyStroke.getKeyStroke(key.toString().replaceAll("\\+", " ").
            replaceAll("Ctrl","ctrl").replaceAll("Alt","alt").replaceAll("Meta","meta").replaceAll("Shift","shift"));
  }/**/

  /**
   * Parse name with mnemonic information.
   * Mnemonic is marked by a single '&', for example "E&xit" will filteredResult in the name "Exit" and the mnemonic 'x'.<br/>
   * Double & will be recognized as a single &, so "&Compile && Run" becomes "Compile & Run" with mnemonic 'C'.<br/>
   * If multiple mnemonic is being marked, the first one will be used.
   *
   * @param name Name to parse.  Becomes parsed name after parsing.
   * @return Parsed mnemonic, or -1 if no mnemonic presents.
   */
  public static int parseName(StringBuffer name) {
    int m = -1;
    StringBuffer result = new StringBuffer(name.length());
    int source = name.length() - 1;
    while (source >= 0) {
      char c = name.charAt(source);
      if (c == '&')
        if (source > 0) // source can be double
          if (name.charAt(source - 1) == '&') { // Double &, insert '&'
            result.insert(0, c);
            source--;
          } else  // Single &
            if (name.length() == source - 1)  // Single, and & is last character
              result.insert(0, c);
            else
              m = name.charAt(source + 1);    // Single, set mnemonic
        else    // source = 0, must be single
          if (name.length() > 1)
            m = name.charAt(1);   // Set mnemonic to 2nd character
          else
            result.insert(0, c);  // or set string to & if it's the only character
      else
        result.insert(0, c);  // Copy normal character
      source--;
    }
    name.replace(0, name.length(), result.toString());
    return m;
  }

  /**
   * Create a label with given name (with mnemonic labeled by &) and right alignment,
   * set it to be label for given component,
   * and return it.
   *
   * @param name Name of the label. e.g. "&Setting"
   * @param c    Comopnent to be targeted by the label
   * @return Created label
   */
  public static JLabel setLabelFor(String name, JComponent c) {
    return setLabelFor(name, c, SwingConstants.RIGHT);
  }

  /**
   * Create a label with given name (with mnemonic labeled by &) and alignment,
   * set it to be label for given component,
   * and return it.
   *
   * @param name      Name of the label. e.g. "&Setting"
   * @param judgement Judgement of the label.  e.g. JLabel.LEFT
   * @param c         Comopnent to be targeted by the label
   * @return Created label
   */
  public static JLabel setLabelFor(String name, JComponent c, int judgement) {
    StringBuffer n = new StringBuffer(name);
    int mnemonic = parseName(n);
    JLabel l = new JLabel(n.toString(), judgement);
    if (mnemonic >= 0) l.setDisplayedMnemonic(mnemonic);
    l.setLabelFor(c);
    return l;
  }

  /**
   * Create a panel with a label on the left and a component on right.
   *
   * @param name Name of the label with mnemonic marked
   * @param c    GigaMain component of the panel
   * @return created JPanel with BorderLayout.
   */
  public static JPanel createLabeledComponent(String name, JComponent c) {
    JPanel p = new JPanel(new BorderLayout());
    p.add(setLabelFor(name, c), BorderLayout.WEST);
    p.add(c);
    return p;
  }

  /**
   * Align labels and components in two columns.
   * @param labels Array of lables to be laid, must be the same size with component array
   * @param comp Array of lables to be laid, must be the same size with label array
   * @return A panel with the lables on the left and component on the right.
   */
  public static JPanel alignInTwoColumns(String[] labels, JComponent[] comp) {
    if (labels.length != comp.length) throw new IllegalArgumentException("Number of labels and component must be the same");
    JLabel[] ls = new JLabel[labels.length];
    for (int i = 0; i < labels.length; i++)
      ls[i] = setLabelFor(labels[i], comp[i]);
    return alignInTwoColumns(ls, comp, 0, 0, null);
  }

  /**
   * Align labels and components in two columns.
   * @param labels Array of labels to be laid, must be the same size with component array
   * @param comp Array of labels to be laid, must be the same size with label array
   * @param padx x padding between labels and components
   * @param pady y padding between rows
   * @param ins Insects of each component
   * @return A panel with the labels on the left and component on the right.
   */
  public static JPanel alignInTwoColumns(JComponent[] labels, JComponent[] comp, int padx, int pady, Insets ins) {
    if (labels.length != comp.length) throw new IllegalArgumentException("Number of labels and component must be the same");
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.ipadx = padx;
    cons.ipady = pady;
    if (ins != null) cons.insets = ins;
    cons.weighty = 1;
    cons.fill = GridBagConstraints.BOTH;
    for (int i = 0; i < labels.length; i++) {
      cons.gridy = i;
      cons.gridx = 0;
      cons.weightx = 0;
      panel.add(labels[i], cons);
      cons.gridx = 1;
      cons.weightx = 1;
      panel.add(comp[i], cons);
    }
    return panel;
  }

  /**
   * Centres given frame on the screen.
   * @param frame Frame to centre
   */
  public static void centreFrame(Frame frame) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle bound = frame.getBounds();
    frame.setLocation((screen.width - bound.width) / 2, (screen.height - bound.height) / 2);
  }

  /**
   * Centres given dialog on the screen.
   * For easier maintaince, this is an overloaded version of centreFrame.
   * @param frame Dialog to centre
   */
  public static void centreFrame(JDialog frame) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle bound = frame.getBounds();
    frame.setLocation((screen.width - bound.width) / 2, (screen.height - bound.height) / 2);
  }

  /**
   * Select all objects in a JList.  Can be operated on an empty list, will return success.
   * 
   * @param list List to select
   * @return true if the operation is success.  False otherwise (e.g. if list or its models are null).
   */
  public static boolean selectAll(JList list) {
    if ((list == null) || (list.getModel() == null) || (list.getSelectionModel() == null) || (list.getModel().getSize() < 0)) return false;
    list.getSelectionModel().setSelectionInterval(0, list.getModel().getSize());
    return true;
  }

  /** A shared create on request empty list d20 instance */
  private static ListModel emptyListModel;

  /** Get an empty Jlist d20
   * @return A shared empty JList d20
   */
  public static ListModel getEmptyListModel() {
    if (emptyListModel == null)
      emptyListModel = new ListModel() {
        public int getSize() {return 0;}
        public Object getElementAt(int index) {return null;}
        public void addListDataListener(ListDataListener l) {}
        public void removeListDataListener(ListDataListener l) {}
      };
    return emptyListModel;
  }

  /**
   * Get the active frame of the application.  Only works for Frames and JFrames.
   * (i.e. Does not work for Dialog, JDialog, or JWindow)
   * @return Owner of active dialog, or the active frame, or null, in order.
   */
  public static Frame getActiveFrame() {
    Frame[] frames = Frame.getFrames();
    for (Frame frame : frames) {
      if (frame.isActive()) return frame;
    }
    return null;
  }

  /**
   * Create a tray icon that shows when given form is iconified and will restore form when acivated.
   * Requires Java 6.0 or upper
   * @param frame Frame to attack the tray icon to, must not be null
   * @param icon Image icon to use, if null then get from frame
   * @param popup Popup menu to attach to the tray icon, may be null
   * @return true if tray is loaded, false otherwise
   */
  public static TrayIcon setMinimiseTray(final JFrame frame, Image icon, PopupMenu popup) {
    try {
      if (Class.forName("java.awt.SystemTray") != null && SystemTray.isSupported()) {
        if (icon == null) icon = frame.getIconImage();
        final TrayIcon trayIcon = new TrayIcon(icon, frame.getTitle(), popup);
        frame.addPropertyChangeListener(new PropertyChangeListener(){ public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("title")) trayIcon.setToolTip(evt.getNewValue().toString());
          }});
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            frame.setVisible(true);
            frame.setState(Frame.NORMAL);
            SystemTray.getSystemTray().remove(trayIcon);
          }
        });
        frame.addWindowListener(new WindowAdapter() {
          public void windowDeiconified(WindowEvent e) {
            SystemTray.getSystemTray().remove(trayIcon);
          }
          public void windowIconified(WindowEvent e) {
            try {
              SystemTray.getSystemTray().add(trayIcon);
              frame.setVisible(false);
            } catch (AWTException e1) {
              e1.printStackTrace();
            }
          }
        });
        return trayIcon;
      }
    } catch (ClassNotFoundException e) {
    }
    return null;
  }
}

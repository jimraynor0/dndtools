package dndbot.ui;

import dndbot.irc.IRC;
import dndbot.module.Log;
import dndbot.module.log.LogRecord;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.sheepy.data.file.BasicFileFilter;
import org.sheepy.stream.DocumentWriter;
import org.sheepy.stream.WriterOutputStream;
import org.sheepy.swing.DocumentAdapter;
import org.sheepy.swing.JAction;
import org.sheepy.swing.TextFieldCache;
import org.sheepy.util.FileUtil;
import org.sheepy.util.StreamUtil;
import org.sheepy.util.StringUtil;
import org.sheepy.util.SwingUtil;
import res.ResourcePath;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.prefs.Preferences;

import static javax.swing.BorderFactory.createEmptyBorder;
import static org.sheepy.util.SwingUtil.scrollBoxOnDemand;
import static org.sheepy.util.SwingUtil.setLabelFor;

/**
 * DnDBot graphical user interface
 *
 * Created by Ho Yiu Yeung on 18-March-2007
 */
public class PnlDiceBot extends JPanel {
  public static PnlDiceBot instance;

  private static final String MISSING_LICENSE = "GPL License ver.3 cannot be loaded.  You can read it from http://www.gnu.org/copyleft/gpl.html\n\n"+
  "This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n"+
  "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.";
  private static final String MISSING_ABOUT = "About cannot be loaded.\n\nDnDBot\nAn IRC bot to assist chatroom campaigns\n(c) 2008 Ho Yiu YEUNG, aka Sheepy\nyour.sheepy@gmail.com\n\nUses PircBot, JEP, Apache Derby, and Jakarta Commons\n\nNon-library source compressed in distributable.\nProgram comes WITHOUT ANY WARRANTY (see license)";
  private static final String MISSING_CHANGELOG = "Change log cannot be loaded.  This is expected when running in IDE instead of with ant built binary.";
  private static final String MONITOR_TAG = Colors.removeFormattingAndColors(IRC.fullVersion)+"\n";

  // Settings
  private int serverIndex = -1; // Currently using server index
  private final JTextField txtServer = new JTextField();
  private final JTextField txtPort = new JTextField();
  private static final Object[] CHARSET = Charset.availableCharsets().keySet().toArray();
  private final JComboBox cboCharset = new JComboBox(CHARSET);
  private final JComboBox cboLogCharset = new JComboBox(CHARSET);
  private final JTextField txtNick = new JTextField();
  private final JTextField txtPass = new JTextField();
  private final JTextField txtChannel = new JTextField();
  private final JTextField txtAdmin = new JTextField();
  private final JComboBox cboLang = new JComboBox(new String[]{"English, en","中文, zh", "简体中文, zh-cn"});
  // Monitor
  private final JTextArea txtOut = new JTextArea(MONITOR_TAG);
  private final JCheckBox chkMonitor = new JCheckBox("Enabled");
  // Console
  private final JTextArea txtConsole = new JTextArea("Bot control console.  Type help for help\n");
  // Log
  private final DefaultListModel lstLogModel = new DefaultListModel();
  private final JList lstLog = new JList(lstLogModel);
  private MouseListener lstLogListener;
  private final JCheckBox chkFilterLog = new JCheckBox("Filter channel traffic", true);
  private JFileChooser dlgLoadLog; // Transform log load dialog
  private JFileChooser dlgSaveLog; // Transform / save log save dialog
  // About
  private final JTextField txtCmd = new TextFieldCache(new JTextField()).getTextField();
  private final JTextArea txtAbout = new JTextArea();
  private String strAbout;
  private String strLicense;
  private String strChangelog;


  IRC bot = null;
  private final JAction actRun = new JAction("&Run") {public void actionPerformed(ActionEvent e) { startBot(); }};
  private final JAction actStop = new JAction("&Stop") {public void actionPerformed(ActionEvent e) { stopBot(); }};
  private final JButton btnRun = new JButton(actRun);
  private static final String PAGE_SETTINGS = "Settings";
  private static final String PAGE_MONITOR = "Monitor";
  private static final String PAGE_CONSOLE = "Console";
  private static final String PAGE_LOG = "Logs";
  private static final String PAGE_ABOUT = "About";

  private JTabbedPane pnlC;
  Preferences pref = Preferences.userNodeForPackage(getClass());

  public PnlDiceBot() {
    super();
    init();
    instance = this;
    // Load settings
    txtServer.setText(pref.get("server", "irc.example.com"));
    txtPort.setText(Integer.toString(pref.getInt("port", 6667)));
    cboCharset.setSelectedItem(pref.get("CHARSET", "UTF-8"));
    txtNick.setText(pref.get("nickname", "DnDBot"));
    txtPass.setText(pref.get("password", ""));
    txtChannel.setText(pref.get("channel", ""));
    txtAdmin.setText(pref.get("admin", ""));
    cboLang.setSelectedItem(pref.get("language", "English, en"));
    cboLogCharset.setSelectedItem(pref.get("log_charset", "UTF-8"));
    chkMonitor.setSelected(pref.getBoolean("monitor_enabled", false));
  }

  /**
   * Start bot and update UI
   */
  public IRC startBot() {
    if (txtServer.getText().length()==0 || txtNick.getText().length() == 0) return null;
    txtServer.setEditable(false);
    txtPort.setEditable(false);
    cboCharset.setEnabled(false);
    txtNick.setEditable(false);
    txtPass.setEditable(false);
    txtChannel.setEditable(false);
//    txtAdmin.setEditable(false);
    btnRun.setAction(actStop);
    JDialog pleaseWait = new JDialog((Frame)this.getTopLevelAncestor(), "Status", false);
    // Determine server
    serverIndex++;
    String[] servers = txtServer.getText().split(";");
    if (serverIndex >= servers.length) serverIndex = 0;
    else if (serverIndex < 0) serverIndex = 0;
    String serverName = servers[serverIndex]; 
    // Try connect
    try {
      Locale l = getBotLocale();
      // Create status window
      pleaseWait.getContentPane().setLayout(new BorderLayout());
      JLabel lblMessage = new JLabel("     Initialising...     ", SwingConstants.CENTER);
      lblMessage.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      pleaseWait.getContentPane().add(lblMessage, SwingConstants.CENTER);
      pleaseWait.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      pleaseWait.pack();
      SwingUtil.centreFrame(pleaseWait);
      pleaseWait.setVisible(true);
      lblMessage.paintImmediately(lblMessage.getVisibleRect());
      // Create / disconnect bot
      if (bot == null) {
            lblMessage.setText("Creating DnDBot...");
            lblMessage.paintImmediately(lblMessage.getVisibleRect());
        bot = new IRC(genRandom(), l);
      } else if (bot.isConnected()) { // Disabled due to issue in reconnecting
            lblMessage.setText("Logging out...");
            lblMessage.paintImmediately(lblMessage.getVisibleRect());
        synchronized(bot) { bot.logout(); }
        // Logout is asynchronous, no exception if failed
        int waitCount = 0;
        while (bot.isConnected() && waitCount <= 5*4) {
          Thread.sleep(250);
          waitCount++;
        }
        if (bot.isConnected()) {
              lblMessage.setText("Error. Recreating DnDBot...");
              lblMessage.paintImmediately(lblMessage.getVisibleRect());
          bot = new IRC(genRandom(), l);
        }
      }
      bot.locale = l;
      updateAdminPassword();
      // Setup and connect
          lblMessage.setText("Resetting...");
          lblMessage.paintImmediately(lblMessage.getVisibleRect());
      bot.charset = cboCharset.getSelectedItem().toString();
      bot.reset();
      bot.setVerbose(chkMonitor.isSelected());
          lblMessage.setText("Connecting...");
          lblMessage.paintImmediately(lblMessage.getVisibleRect());
      bot.login(serverName, StringUtil.strToInt(txtPort.getText(), 6667), txtNick.getText(), txtPass.getText());
      if (txtChannel.getText().length() > 0) {
        if (txtChannel.getText().charAt(0) != '#') txtChannel.setText("#"+txtChannel.getText());
        bot.joinChannel(txtChannel.getText());
      }
      pleaseWait.dispose();
      pnlC.setSelectedIndex(pnlC.indexOfTab(PAGE_CONSOLE));

      // On successful connect, save settings
      pref.put("server", txtServer.getText());
      pref.putInt("port", StringUtil.strToInt(txtPort.getText(), 6667));
      pref.put("CHARSET", cboCharset.getSelectedItem().toString());
      pref.put("nickname", txtNick.getText());
      pref.put("password", txtPass.getText());
      pref.put("channel", txtChannel.getText());
      pref.put("admin", txtAdmin.getText());
      pref.put("language", cboLang.getSelectedItem().toString());

    } catch(NickAlreadyInUseException e) {
      JOptionPane.showMessageDialog(this, "Someone is using that nickname.  Rename me and try again.\n\nDetails:\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
      stopBot();

    } catch(IrcException e) {
      JOptionPane.showMessageDialog(this, "Connected but cannot login.  Please check nickname and password.\n\nDetails:\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
      stopBot();

    } catch(SocketException e) {
      JOptionPane.showMessageDialog(this, "Cannot connect.  Are you sure server name and port are correct?\n\nDetails:\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
      stopBot();

    } catch(Exception e) {
      JOptionPane.showMessageDialog(this, "An error occured.\n\nDetails:\n"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
      stopBot();

    } finally {
      pleaseWait.dispose();
    }
    
    return bot;
  }

  private static Random genRandom() {
    return new SecureRandom();
  }

  /**
   * Disconnect bot and update UI
   */
  public void stopBot() {
    // Restore fields
    txtServer.setEditable(true);
    txtPort.setEditable(true);
    cboCharset.setEnabled(true);
    txtNick.setEditable(true);
    txtPass.setEditable(true);
    txtChannel.setEditable(true);
    btnRun.setAction(actRun);

    // Save settings
    pref.put("admin", txtAdmin.getText());
    pref.put("language", cboLang.getSelectedItem().toString());
    pref.putBoolean("monitor_enabled", chkMonitor.isSelected());

    // Stop bot once all done.  Just in case we actually fail.
    if (bot != null) synchronized(bot) { bot.logout(); }
    pnlC.setSelectedIndex(pnlC.indexOfTab(PAGE_SETTINGS));
  }

  private void init() {
    removeAll();
    setLayout(new BorderLayout());

    add(btnRun, BorderLayout.SOUTH);
    add(new JLabel(Colors.removeFormattingAndColors(IRC.version), SwingConstants.CENTER), SwingConstants.NORTH);
    setBorder(createEmptyBorder(3,3,3,3));
    pnlC = new JTabbedPane(SwingConstants.TOP);

    /** Setting panel */
    JPanel pnlC1 = new JPanel(new BorderLayout(5, 5));
    JPanel pnlC1L = new JPanel(new GridLayout(8, 1));
    JPanel pnlC1R = new JPanel(new GridLayout(8, 1));
    pnlC1.add(pnlC1L, BorderLayout.WEST);
    pnlC1.add(pnlC1R);
    pnlC1R.add(txtServer);
    pnlC1L.add(setLabelFor("S&erver", txtServer));
    pnlC1R.add(txtPort);
    pnlC1L.add(setLabelFor("P&ort", txtPort));
    pnlC1R.add(cboCharset);
    pnlC1L.add(setLabelFor("&Charset", cboCharset));
    pnlC1R.add(txtNick);
    pnlC1L.add(setLabelFor("&Nickname", txtNick));
    pnlC1R.add(txtPass);
    pnlC1L.add(setLabelFor("&Password", txtPass));
    pnlC1R.add(txtChannel);
    pnlC1L.add(setLabelFor("&Channel", txtChannel));
    pnlC1R.add(txtAdmin);
    pnlC1L.add(setLabelFor("&Admin Pwd", txtAdmin));
    pnlC1R.add(cboLang);
    pnlC1L.add(setLabelFor("&Language", cboLang));
    pnlC1.setBorder(createEmptyBorder(5,5,5,5));
    txtNick.getDocument().addDocumentListener(new DocumentAdapter(){
      public void contentUpdate(DocumentEvent e) {
        refreshTitle();
      }});
    txtAdmin.getDocument().addDocumentListener(new DocumentAdapter(){
      public void contentUpdate(DocumentEvent e) {
        updateAdminPassword();
      }});
    cboLang.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        if (bot == null) return;
        bot.locale = getBotLocale();
        synchronized(bot) { bot.reset(); }
        pref.put("language", cboLang.getSelectedItem().toString());
      }});
//    pnlC1.setBorder(createCompoundBorder(createTitledBorder(createEtchedBorder(),"Settings"), createEmptyBorder(0,5,5,5)));

    /** Monitor panel */
    JPanel pnlC2 = new JPanel(new BorderLayout());
    JPanel pnlC2T = new JPanel(new GridLayout(1, 2));
    chkMonitor.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
      if (bot != null) bot.setVerbose(chkMonitor.isSelected());
      }});
    pnlC2T.add(chkMonitor);
    pnlC2T.add(new JButton(new JAction("&Clear"){ public void actionPerformed(ActionEvent e) {
      try {
        ((AbstractDocument)txtOut.getDocument()).replace(0, txtOut.getDocument().getLength()-1, MONITOR_TAG, null);
      } catch (BadLocationException e1) {
        e1.printStackTrace();
      }
    }}));
    pnlC2.add(pnlC2T, BorderLayout.NORTH);
    pnlC2.add(scrollBoxOnDemand(txtOut));
    txtOut.setEditable(false);
    try {
      System.setOut(new PrintStream(new WriterOutputStream(new DocumentWriter(txtOut), Charset.forName("UTF-8")), true, "UTF-8"));
    } catch (UnsupportedEncodingException e2) {
      e2.printStackTrace();
      System.setOut(new PrintStream(new WriterOutputStream(new DocumentWriter(txtOut))));
    }
    System.setErr(System.out);

    /** Console panel */
    final JPanel pnlC3 = new JPanel(new BorderLayout());
    pnlC3.add(scrollBoxOnDemand(txtConsole));
    txtConsole.setEditable(false);
    txtCmd.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        if (txtConsole.getText().length() <= 0) return;
        // Create bot if none exists
        if (bot == null) bot = new IRC(genRandom(), getBotLocale());
        // Add command to console
        txtConsole.append("> "+txtCmd.getText()+"\n");
        // Send command for processing
        bot.onPrivateMessage(IRC.USER_GUI, IRC.USER_GUI, "localhost", txtCmd.getText());
        txtCmd.setText(""); // Cleanup
      }});
    pnlC3.add(txtCmd, BorderLayout.SOUTH);
    pnlC3.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        txtCmd.requestFocusInWindow();
      }
    });

    /** Log panel */
    final JPanel pnlC4 = new JPanel(new BorderLayout());
    JPanel pnlC4T = new JPanel(new BorderLayout());
    JPanel pnlC4B = new JPanel(new BorderLayout());
    JPanel pnlC4BR = new JPanel(new BorderLayout());
    pnlC4.add(scrollBoxOnDemand(lstLog));
    lstLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    pnlC4.add(pnlC4T, BorderLayout.NORTH);
    pnlC4.add(pnlC4B, BorderLayout.SOUTH);
    pnlC4T.add(new JLabel("Double click log to save"));
    pnlC4T.add(new JButton(new JAction("&Refresh list", null, null, "Refresh log list") {
      public void actionPerformed(ActionEvent e) {
        refreshLog();
      }}), BorderLayout.EAST);
    pnlC4B.add(new JButton(new JAction("&Convert existing log", null, null, "Convert IRC encoded log into other format") {
      public void actionPerformed(ActionEvent e) {
        convertLog();
      }}));
    pnlC4B.add(pnlC4BR, BorderLayout.EAST);
    pnlC4BR.add(chkFilterLog, BorderLayout.NORTH);
    pnlC4BR.add(new JLabel("Input:"), BorderLayout.WEST);
    pnlC4BR.add(cboLogCharset, BorderLayout.CENTER);
    pnlC4BR.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
    // Add listener that export log on double click
    if (lstLogListener == null) {
      lstLogListener = new MouseAdapter(){
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) saveLog();
        }};
      lstLog.addMouseListener(lstLogListener);
    }
    /* // Save button replaced by double click
    pnlC4.add(new JButton(new JAction("&Save"){
      public void actionPerformed(ActionEvent e) {
      }}), BorderLayout.SOUTH);*/

    /** About panel */
    JPanel pnlC5 = new JPanel(new BorderLayout());
    pnlC5.add(scrollBoxOnDemand(txtAbout));
    final JComboBox cboAbout = new JComboBox(new String[]{"About","License","Change log"});
    pnlC5.add(cboAbout, BorderLayout.NORTH);
    txtAbout.setEditable(false);
    txtAbout.setLineWrap(true);
    txtAbout.setWrapStyleWord(true);
    txtAbout.setTabSize(4);
    strAbout = getResourceFileAsString("about.txt", MISSING_ABOUT);
    txtAbout.setText(strAbout);
    cboAbout.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) {
        String text = strAbout;
        if (cboAbout.getSelectedIndex() == 1) { // License
          if (strLicense == null) strLicense = getResourceFileAsString("license.txt", MISSING_LICENSE);
          text = strLicense;
        } else if (cboAbout.getSelectedIndex() == 2) { // Change log
          if (strChangelog == null) strChangelog = getResourceFileAsString("change.log", MISSING_CHANGELOG);
          text = strChangelog;
        }
        txtAbout.setText(text);
        txtAbout.setCaretPosition(0);
      }});

    // All panels!
    add(pnlC);
    pnlC.add(pnlC1, PAGE_SETTINGS);
    pnlC.add(pnlC2, PAGE_MONITOR);
    pnlC.add(pnlC3, PAGE_CONSOLE);
    pnlC.add(pnlC4, PAGE_LOG);
    pnlC.add(pnlC5, PAGE_ABOUT);
    pnlC.setBorder(createEmptyBorder(0, 0, 5, 0));
    pnlC.addChangeListener(new ChangeListener(){ public void stateChanged(ChangeEvent e) {
      if (pnlC.getSelectedComponent() == pnlC4)
        refreshLog();
    }});
    addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        refreshLog();
      }});
    refreshTitle();
  }

  /**
   * Append given *complete* message to console
   * @param message
   */
  public void addConsole(String message) {
    txtConsole.append(Colors.removeFormattingAndColors(message+"\n"));
    txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
  }

  /** Read a document resource with default message on fail
   * @param file Filename to read
   * @param missing String to use on missing resource
   * @return Either file as string or missing string */
  private String getResourceFileAsString(String file, String missing) {
    try {
      return StreamUtil.readAllAsString(ResourcePath.class.getResource(file).openConnection().getInputStream(), Charset.forName("UTF-8"));
    } catch (UnsupportedEncodingException e2) {
      e2.printStackTrace();
      try {
        return StreamUtil.readAllAsString(ResourcePath.class.getResource(file).openConnection().getInputStream());
      } catch (IOException e) {
        e.printStackTrace();
        return missing;
      }
    } catch (NullPointerException e1) {
      return missing;
    } catch (Exception e1) {
      e1.printStackTrace();
      return missing;
    }
  }

  /** Update bot admin password */
  private void updateAdminPassword() {
    if (bot == null) return;
    bot.data.put(IRC.KEY_ADMIN_PASSWORD, txtAdmin.getText());
    bot.settings.put(IRC.USER_GUI, IRC.KEY_ADMIN_PASSWORD, txtAdmin.getText());
    pref.put("admin", txtAdmin.getText());
  }

  /** Get cboLang's locale
   * @return Currently selected locale */
  private Locale getBotLocale() {
    return new Locale(cboLang.getSelectedItem().toString().split(",")[1].trim().replace('-', '_'));
  }

  /** Refresh log list */
  @SuppressWarnings("unchecked")
  private void refreshLog() {
    if (lstLogModel.getSize() > 0) lstLogModel.clear();
    if (bot == null) return;
    Map<String, LogRecord> log = (Map<String, LogRecord>) bot.data.get(Log.DATA_LOGS);
    synchronized (log) {
      for (LogRecord l : log.values()) {
        lstLogModel.addElement(l);
      }
    }
  }

  /** Check that log save dialog is created */
  private void checkSaveDialog() {
    if (dlgSaveLog == null) {
      dlgSaveLog = new JFileChooser();
      // First word must be format to use, since that is how String type will be acquired
      dlgSaveLog.setFileFilter(new BasicFileFilter("xml","XML ($EXT)"));
      FileFilter def = new BasicFileFilter("bbc","BBC - Bulletin Board Code ($EXT)");
      dlgSaveLog.addChoosableFileFilter(def);
      dlgSaveLog.addChoosableFileFilter(new BasicFileFilter("txt","TXT - Plain text ($EXT)"));
      dlgSaveLog.addChoosableFileFilter(new BasicFileFilter("htm;html;xhtml","HTML ($EXT)"));
      dlgSaveLog.setFileFilter(def);
    }
  }

  /** Save a single selected log */
  private void saveLog() {
    if (lstLog.getSelectedValue() == null) return;
    checkSaveDialog();
    if (dlgSaveLog.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
    File f = dlgSaveLog.getSelectedFile();
    if (f.exists())
      if (JOptionPane.showConfirmDialog(this, "File exists.  Override?") != JOptionPane.OK_OPTION)
        return;
    String type = StringUtil.firstWords(dlgSaveLog.getFileFilter().getDescription(), 1).toLowerCase();
    if (!dlgSaveLog.getFileFilter().accept(f)) {
      f = new File(f.toString()+"."+type);
    }
    try {
      Log.saveLog(((LogRecord)lstLog.getSelectedValue()).get(), type, f);
      JOptionPane.showMessageDialog(this, "Save success", "Done", JOptionPane.INFORMATION_MESSAGE);
    } catch (UnsupportedFlavorException e1) {
      JOptionPane.showMessageDialog(this, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (TransformerException e1) {
      JOptionPane.showMessageDialog(this, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e1) {
      JOptionPane.showMessageDialog(this, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /** Convert IRC log into other formats */
  private void convertLog() {
    if (dlgLoadLog == null) {
      dlgLoadLog = new JFileChooser();
      dlgLoadLog.addChoosableFileFilter(new BasicFileFilter("txt;log","Logs ($EXT)"));
    }
    if (dlgLoadLog.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
    checkSaveDialog();
    if (dlgSaveLog.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
    File in = dlgLoadLog.getSelectedFile();
    File out = dlgSaveLog.getSelectedFile();
    String type = StringUtil.firstWords(dlgSaveLog.getFileFilter().getDescription(), 1).toLowerCase();
    if (!dlgSaveLog.getFileFilter().accept(out))  out = new File(out.toString()+"."+type);
    if (chkFilterLog.isSelected()) type += "-filtered";
    String log = FileUtil.asString(in, cboLogCharset.getSelectedItem().toString());
    if (log != null) log = log.trim();
    if (StringUtil.isEmpty(log)) {
      JOptionPane.showMessageDialog(this, "Cannot read log or log is empty", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      // Remove BOM
      if (log.startsWith("\uFEFF")) log = log.substring(1);
      else if (log.startsWith("\uEFBB")) log = log.substring(2);
      log = log.replace("\r\n", "\n").trim();
      log = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><log><message time='' sender=''>"+
              Log.translateAll(log.replace('\u0000', ' ')).replace("\n","</message><message time='' sender=''>")+
              "</message></log></xml>";
      Log.saveLog(log, type, out);
      pref.put("log_charset", cboLogCharset.getSelectedItem().toString());
      JOptionPane.showMessageDialog(this, "Convert success.", "Done", JOptionPane.INFORMATION_MESSAGE);
    } catch (TransformerConfigurationException e1) {
      e1.printStackTrace();
      JOptionPane.showMessageDialog(this, "Cannot convert (see monitor for details).\nPlease check that the transform is correct xslt.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (TransformerException e1) {
      e1.printStackTrace();
      JOptionPane.showMessageDialog(this, "Cannot convert (see monitor for details).\nPlease check that the encoding is correct.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e1) {
      e1.printStackTrace();
      JOptionPane.showMessageDialog(this, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /** Refresh parent frame title */
  public void refreshTitle() {
    Container top = PnlDiceBot.this.getTopLevelAncestor();
    if (top != null && top instanceof Frame) {
      Frame win = (Frame)top;
      if (win.getTitle().contains("DnDBot")) win.setTitle(Colors.removeFormattingAndColors(txtNick.getText()+" - "+IRC.version));
    }
  }

  /* Set to enable or disable logging */
  public void setMonitor(boolean enabled) {
    chkMonitor.setSelected(enabled);
  }

}
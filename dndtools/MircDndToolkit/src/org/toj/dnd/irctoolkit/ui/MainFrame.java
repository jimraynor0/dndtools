package org.toj.dnd.irctoolkit.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.ui.map.filterpane.MapFilterPane;
import org.toj.dnd.irctoolkit.ui.map.mappane.MapGridPanel;
import org.toj.dnd.irctoolkit.ui.map.modelpane.MapModelPane;
import org.toj.dnd.irctoolkit.ui.map.viewpane.PcViewPanel;
import org.toj.dnd.irctoolkit.ui.menu.IrcToolkitMenu;

import com.alee.laf.WebLookAndFeel;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 5410456877957941322L;

    private static Logger log = Logger.getLogger(MainFrame.class);

    private ReadonlyContext context;
    private MapGridPanel mapPanel;
    private MapModelPane modelPanel;
    private PcViewPanel viewPanel;
    private MapFilterPane filterPanel;

    public MainFrame(ReadonlyContext context) {
        this.context = context;
        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            WebLookAndFeel.install();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(
                        "Dialog", Font.PLAIN, 10));
                // log.debug("" + key + UIManager.get(key));
            }
        }

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane jTabbedPane = new JTabbedPane();
        this.getContentPane().add(jTabbedPane);

        jTabbedPane
                .addTab("<html><body leftmargin=12 topmargin=2 marginwidth=12 marginheight=2><font size=+0>ªÊ÷∆</font></body></html>",
                        createEditingPanel());
        jTabbedPane
                .addTab("<html><body leftmargin=12 topmargin=2 marginwidth=12 marginheight=2><font size=+0>’πœ÷</font></body></html>",
                        createViewingPanel());

        createMenuBar();
    }

    private JComponent createEditingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        createModelPanel(panel, createMapPanel(panel));

        return panel;
    }

    private JComponent createViewingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        createViewPanel(panel);
        createFilterPanel(panel);
        return panel;
    }

    public void createViewPanel(JPanel panel) {
        viewPanel = new PcViewPanel(context);
        panel.add(new JScrollPane(viewPanel), BorderLayout.CENTER);
    }

    private void createFilterPanel(JPanel panel) {
        this.filterPanel = new MapFilterPane(context);
        panel.add(filterPanel, BorderLayout.EAST);
    }

    public MapGridPanel createMapPanel(JPanel panel) {
        mapPanel = new MapGridPanel(context);
        panel.add(new JScrollPane(mapPanel), BorderLayout.CENTER);
        return mapPanel;
    }

    private void createModelPanel(JPanel panel, MapGridPanel mapGridPanel) {
        this.modelPanel = new MapModelPane(context, mapGridPanel);
        panel.add(modelPanel, BorderLayout.EAST);
    }

    private void createMenuBar() {
        this.add(new IrcToolkitMenu(this), BorderLayout.NORTH);
    }

    public void launch() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(800, 600);
        this.setSize(frameSize);
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
    }
}

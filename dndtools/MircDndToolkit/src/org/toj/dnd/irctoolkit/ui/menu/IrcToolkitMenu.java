package org.toj.dnd.irctoolkit.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.toj.dnd.irctoolkit.configs.GlobalConfigs;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.ui.GenerateRandomMapCommand;
import org.toj.dnd.irctoolkit.engine.command.ui.LoadMapFromFileCommand;
import org.toj.dnd.irctoolkit.engine.command.ui.MapUndoCommand;
import org.toj.dnd.irctoolkit.engine.command.ui.SaveMapToFileCommand;
import org.toj.dnd.irctoolkit.io.pircbot.IrcClient;
import org.toj.dnd.irctoolkit.mapgenerator.ca.CelullarAutomationCaveGenerator;
import org.toj.dnd.irctoolkit.mapgenerator.dla.DiffusionLimitedAggregationCaveGenerator;
import org.toj.dnd.irctoolkit.mapgenerator.doorfirst.DoorFirstDungeonGenerator;
import org.toj.dnd.irctoolkit.ui.MainFrame;
import org.toj.dnd.irctoolkit.ui.console.ConsolePane;

public class IrcToolkitMenu extends JMenuBar {

    private static final long serialVersionUID = -1414715154183290437L;
    private Logger log = Logger.getLogger(this.getClass());

    private MainFrame frame;
    private NewMapDialog newMapDialog;
    private JFileChooser fileChooser;

    private String getMapDir() {
        File mapDir = new File("maps");
        if (!mapDir.isDirectory()) {
            mapDir.mkdirs();
        }
        return mapDir.getAbsolutePath();
    }

    public IrcToolkitMenu(MainFrame mainFrame) {
        this.frame = mainFrame;
        this.newMapDialog = new NewMapDialog(frame);
        // RestrainedFileSystemView fsv = new RestrainedFileSystemView(new
        // File(this.getMapDir()));
        // this.fileChooser = new JFileChooser(fsv.getHomeDirectory(), fsv);
        this.fileChooser = new JFileChooser(getMapDir());
        this.fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".map");
            }

            @Override
            public String getDescription() {
                return "DM Toolkit 地图 (*.map)";
            }
        });

        JMenu fileMenu = new JMenu("文件");

        JMenuItem newMap = new JMenuItem("新建地图", KeyEvent.VK_N);
        newMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                ActionEvent.ALT_MASK));
        newMap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newMapDialog.launch();
            }
        });
        fileMenu.add(newMap);

        JMenuItem loadFromFile = new JMenuItem("读取地图", KeyEvent.VK_F);
        loadFromFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                ActionEvent.CTRL_MASK));
        loadFromFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showDialog(frame, "读取地图");
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    ToolkitEngine.getEngine().queueCommand(
                            new LoadMapFromFileCommand(file));
                }
            }
        });
        fileMenu.add(loadFromFile);

        JMenuItem SaveToFile = new JMenuItem("保存地图", KeyEvent.VK_S);
        SaveToFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        SaveToFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (!file.getName().endsWith(".map")) {
                        file = new File(file.getParent(), file.getName()
                                + ".map");
                    }
                    ToolkitEngine.getEngine().queueCommand(
                            new SaveMapToFileCommand(file));
                }
            }
        });
        fileMenu.add(SaveToFile);
        //
        // JMenuItem loadFromClipboard = new JMenuItem("Load from Clipboard",
        // KeyEvent.VK_C);
        // loadFromClipboard.addActionListener(new ActionListener() {
        //
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // ToolkitEngine.getEngine().queueCommand(new
        // LoadFromClipboardCommand());
        // }
        // });
        // fileMenu.add(loadFromClipboard);

        fileMenu.addSeparator();

        JMenuItem undo = new JMenuItem("撤销操作", KeyEvent.VK_U);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                ActionEvent.CTRL_MASK));
        undo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(new MapUndoCommand());
            }
        });
        fileMenu.add(undo);

        this.add(fileMenu);

        JMenu mapGenMenu = new JMenu("随机地图");

        JMenuItem generateDungeon = new JMenuItem("随机生成地城", KeyEvent.VK_D);
        generateDungeon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                ActionEvent.ALT_MASK));
        generateDungeon.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(
                        new GenerateRandomMapCommand(
                                new DoorFirstDungeonGenerator()));
            }
        });
        mapGenMenu.add(generateDungeon);

        JMenuItem generateNaturalCave = new JMenuItem("随机生成平滑洞穴", KeyEvent.VK_C);
        generateNaturalCave.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.ALT_MASK));
        generateNaturalCave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine.getEngine().queueCommand(
                        new GenerateRandomMapCommand(
                                new CelullarAutomationCaveGenerator()));
            }
        });
        mapGenMenu.add(generateNaturalCave);

        JMenuItem generateRuggedCave = new JMenuItem("随机生成崎岖洞穴", KeyEvent.VK_R);
        generateRuggedCave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                ActionEvent.ALT_MASK));
        generateRuggedCave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ToolkitEngine
                        .getEngine()
                        .queueCommand(
                                new GenerateRandomMapCommand(
                                        new DiffusionLimitedAggregationCaveGenerator()));
            }
        });
        mapGenMenu.add(generateRuggedCave);
        this.add(mapGenMenu);

        JMenu ircMenu = new JMenu("IRC");

        JMenuItem connectToIrc = new JMenuItem("连接至IRC", KeyEvent.VK_C);
        connectToIrc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String result = JOptionPane
                            .showInputDialog(
                                    "请输入主机地址(格式为\"主机名:端口号\"，例如\"irc.ourirc.com:8080\")",
                                    IrcClient.getInstance().getServer() + ":"
                                            + IrcClient.getInstance().getPort());
                    if (result != null && !result.isEmpty()) {
                        String[] params = result.split("\\:");
                        if (params.length == 1) {
                            GlobalConfigs.getConfigs()
                                    .set(GlobalConfigs.CONF_IRC_SERV_HOST,
                                            params[0]);
                        } else {
                            GlobalConfigs.getConfigs()
                                    .set(GlobalConfigs.CONF_IRC_SERV_HOST,
                                            params[0]);
                            GlobalConfigs.getConfigs()
                                    .set(GlobalConfigs.CONF_IRC_SERV_PORT,
                                            params[1]);
                        }
                        if (IrcClient.getInstance().isConnected()) {
                            IrcClient.getInstance().disconnect();
                        }
                        IrcClient.getInstance().connect();
                    }
                } catch (NickAlreadyInUseException e1) {
                    ToolkitEngine.getEngine().fireErrorMsgWindow(
                            "DM Toolkit的昵称\""
                                    + IrcClient.getInstance().getNick()
                                    + "\"已经被占用了。");
                } catch (IOException e1) {
                    ToolkitEngine.getEngine()
                            .fireErrorMsgWindow("无法连接到IRC服务器。");
                } catch (IrcException e1) {
                    ToolkitEngine.getEngine()
                            .fireErrorMsgWindow("无法连接到IRC服务器。");
                }
            }
        });
        ircMenu.add(connectToIrc);

        JMenuItem changeNick = new JMenuItem("更改昵称", KeyEvent.VK_N);
        changeNick.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String nick = IrcClient.getInstance().getNickname();
                String result = JOptionPane.showInputDialog("请输入新的昵称", nick);
                if (result != null && !result.isEmpty()) {
                    log.debug("changing nick from " + nick + " to " + result);
                    IrcClient.getInstance().setNickname(result);
                    GlobalConfigs configs = GlobalConfigs.getConfigs();
                    configs.set(GlobalConfigs.CONF_IRC_NICKNAME_ALT,
                            configs.get(GlobalConfigs.CONF_IRC_NICKNAME));
                    configs.set(GlobalConfigs.CONF_IRC_NICKNAME, result);
                }
            }
        });
        ircMenu.add(changeNick);

        this.add(ircMenu);

        JMenu windowMenu = new JMenu("窗口");
        JMenuItem openConsole = new JMenuItem("控制台", KeyEvent.VK_C);
        openConsole.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ConsolePane.getInstance().adjustPosition();
                ConsolePane.getInstance().setVisible(true);
            }
        });
        windowMenu.add(openConsole);
        this.add(windowMenu);
    }
}

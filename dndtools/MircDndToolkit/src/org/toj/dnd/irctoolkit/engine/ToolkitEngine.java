package org.toj.dnd.irctoolkit.engine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.toj.dnd.irctoolkit.configs.GlobalConfigs;
import org.toj.dnd.irctoolkit.engine.command.Command;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.ui.LoadMapFromFileCommand;
import org.toj.dnd.irctoolkit.engine.command.ui.NewMapCommand;
import org.toj.dnd.irctoolkit.engine.observers.FilterListObserver;
import org.toj.dnd.irctoolkit.engine.observers.MapGridObserver;
import org.toj.dnd.irctoolkit.engine.observers.ModelListObserver;
import org.toj.dnd.irctoolkit.engine.observers.PcViewObserver;
import org.toj.dnd.irctoolkit.io.pircbot.IrcClient;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.ui.MainFrame;
import org.toj.dnd.irctoolkit.ui.console.ConsolePane;

public class ToolkitEngine extends Thread {

    public static final String MAIN_FRAME_TITLE = "Jim's mirc Toolkit";
    public static final String ERR_MSG_TITLE = "好吧，我败了……";
    private static ToolkitEngine INSTANCE;

    public static ToolkitEngine getEngine() {
        if (INSTANCE == null) {
            INSTANCE = new ToolkitEngine();
        }
        return INSTANCE;
    }

    private Logger log = Logger.getLogger(this.getClass());

    private ToolkitEngine() {
        super("Toolkit-Engine");
        this.setDaemon(true);
    }

    private ToolkitContext context = ToolkitContext.getContext();;
    private LinkedList<MapGridObserver> mapGridObservers = new LinkedList<MapGridObserver>();
    private LinkedList<ModelListObserver> modelListObservers = new LinkedList<ModelListObserver>();
    private LinkedList<PcViewObserver> pcViewObservers = new LinkedList<PcViewObserver>();
    private LinkedList<FilterListObserver> filterListObservers = new LinkedList<FilterListObserver>();
    private BlockingQueue<Command> cmdQueue = new LinkedBlockingQueue<Command>();
    private MainFrame mainFrame;

    @Override
    public void run() {
        while (true) {
            try {
                log.debug("waiting for next command...");
                Command cmd = cmdQueue.take();
                log.debug("command retrieved: " + cmd.getClass());
                if (cmd.undoable()) {
                    context.getHistory().pushCmd(cmd);
                }
                List<OutgoingMsg> msgs = null;
                try {
                    msgs = cmd.execute();
                } catch (ToolkitWarningException e) {
                    if (cmd instanceof GameCommand) {
                        GameCommand ircCmd = ((GameCommand) cmd);
                        OutgoingMsg msg = new OutgoingMsg(ircCmd.getChan(),
                                ircCmd.getCaller(), e.getMessage(),
                                OutgoingMsg.WRITE_TO_MSG, null, -1);
                        msgs = Arrays.asList(msg);
                    }
                }
                processOutgoingMsg(cmd, msgs);
            } catch (Exception e) {
                log.error("error during execution: ", e);
            }
        }
    }

    private void processOutgoingMsg(Command cmd, List<OutgoingMsg> msgs) {
        if (cmd.isProcessResponse()) {
            ConsolePane.getInstance().processOutgoingMsgs(msgs);
            IrcClient.getInstance().processOutgoingMsgs(msgs);
        }
    }

    public void launch() {
        GlobalConfigs.getConfigs();
        Command.init(context);
        this.start();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mainFrame = new MainFrame(context);
                mainFrame.launch();
                // setDisconnected();
            }
        });
        try {
            String lastMapFileName = GlobalConfigs.getConfigs().get(
                    GlobalConfigs.CONF_LAST_MAP);
            if (lastMapFileName != null && !lastMapFileName.isEmpty()
                    && new File(lastMapFileName).isFile()) {
                this.queueCommand(new LoadMapFromFileCommand(new File(
                        lastMapFileName)));
            } else {
                this.queueCommand(new NewMapCommand(20, 20));
            }
        } catch (Exception e) {
            this.queueCommand(new NewMapCommand(20, 20));
        }

        try {
            IrcClient.getInstance().connect();
        } catch (NickAlreadyInUseException e) {
            fireErrorMsgWindow("DM Toolkit的昵称\""
                    + IrcClient.getInstance().getNick() + "\"已经被占用了。");
            setDisconnected();
        } catch (IOException e) {
            fireErrorMsgWindow("无法连接到IRC服务器。");
            setDisconnected();
        } catch (IrcException e) {
            fireErrorMsgWindow("无法连接到IRC服务器。");
            setDisconnected();
        }
    }

    public void fireErrorMsgWindow(String errorMsg) {
        JOptionPane.showMessageDialog(mainFrame, errorMsg, ERR_MSG_TITLE,
                JOptionPane.ERROR_MESSAGE);
    }

    public void setDisconnected() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame.setTitle(MAIN_FRAME_TITLE + " - Disconnected");
            }
        });
    }

    public void setConnected() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IrcClient irc = IrcClient.getInstance();
                mainFrame.setTitle(MAIN_FRAME_TITLE + " - Connected : "
                        + irc.getNickname() + "@" + irc.getServer() + ":"
                        + irc.getPort());
            }
        });
    }

    public void addMapGridObserver(MapGridObserver ob) {
        this.mapGridObservers.add(ob);
    }

    public void addModelListObserver(ModelListObserver ob) {
        this.modelListObservers.add(ob);
    }

    public void addPcViewObserver(PcViewObserver ob) {
        this.pcViewObservers.add(ob);
    }

    public void addFilterListObserver(FilterListObserver ob) {
        this.filterListObservers.add(ob);
    }

    public void queueCommand(Command c) {
        cmdQueue.add(c);
    }

    public void fireMapChangeEvent() {
        this.context.getCurrentMap().forceRefresh();
        for (final MapGridObserver mapOb : this.mapGridObservers) {
            mapOb.update(context.getCurrentMap());
        }
        fireViewChangeEvent();
    }

    public void fireModelChangeEvent() {
        for (final ModelListObserver modelOb : this.modelListObservers) {
            modelOb.update(context);
        }
        fireMapChangeEvent();
    }

    public void fireViewChangeEvent() {
        for (final PcViewObserver ob : this.pcViewObservers) {
            ob.update(context);
        }
    }

    public void fireFilterChangeEvent() {
        for (final FilterListObserver ob : this.filterListObservers) {
            ob.update(context);
        }
        fireViewChangeEvent();
    }

    public ToolkitContext getContext() {
        return context;
    }
}

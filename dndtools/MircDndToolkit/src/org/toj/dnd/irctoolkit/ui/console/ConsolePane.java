package org.toj.dnd.irctoolkit.ui.console;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;
import org.jibble.pircbot.Colors;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.Command;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.ui.StyleConstants;

public class ConsolePane extends JDialog {
    private static final long serialVersionUID = -5034705087218383053L;

    private static ConsolePane instance = new ConsolePane();

    public static ConsolePane getInstance() {
        return instance;
    }

    private Logger log = Logger.getLogger(this.getClass());

    private JTextArea taLog;
    private JTextField tfInput;

    private String topicCache;

    public ConsolePane() {
        this.setTitle("控制台");
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createConsoleLog(), BorderLayout.CENTER);
        contentPane.add(createConsoleInput(), BorderLayout.SOUTH);
        contentPane.setPreferredSize(StyleConstants.CONSOLE_DIALOG_SIZE);
    }

    private JComponent createConsoleLog() {
        taLog = new JTextArea();
        taLog.setLineWrap(true);
        taLog.setEditable(false);

        JScrollPane container = new JScrollPane(taLog);
        container
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        container
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        container.setPreferredSize(StyleConstants.CONSOLE_LOG_SIZE);
        return container;
    }

    private JComponent createConsoleInput() {
        tfInput = new JTextField();
        tfInput.setPreferredSize(StyleConstants.CONSOLE_INPUT_SIZE);
        tfInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                append(tfInput.getText());
                queueCommand(tfInput.getText());
                tfInput.setText("");
            }
        });
        tfInput.requestFocus();
        return tfInput;
    }

	private void queueCommand(String cmd) {
        if (cmd.startsWith(".")) {
            if (log.isDebugEnabled()) {
                log.debug("Command received from console: " + cmd);
            }
            String actor = "DM";
            try {
                Command c = IrcCommandFactory.buildCommand(
                        new StringBuilder(cmd).append(" ").append("CONSOLE")
                                .append(" ").append(actor).toString(), null, 0);
                if (c == null) {
                    log.warn("Command dropped: invalid command.");
                } else {
                    ToolkitEngine.getEngine().queueCommand(c);
                }
            } catch (Exception e) {
                log.warn("Exception while parsing cmd: ", e);
            }
        }
	}

    private Game getGame() {
        return ToolkitEngine.getEngine().getContext().getGame();
    }

    public void adjustPosition() {
        setBounds(calculatePosition());
    }

    private Rectangle calculatePosition() {
        Point pCenter = java.awt.MouseInfo.getPointerInfo().getLocation();
        Rectangle bounds = new Rectangle();
        bounds.x = pCenter.x - StyleConstants.CONSOLE_DIALOG_SIZE.width / 2;
        if (bounds.x < 0) {
            bounds.x = 0;
        }
        bounds.y = pCenter.y - StyleConstants.CONSOLE_DIALOG_SIZE.height / 2;
        if (bounds.y < 0) {
            bounds.y = 0;
        }
        bounds.width = StyleConstants.CONSOLE_DIALOG_SIZE.width;
        bounds.height = StyleConstants.CONSOLE_DIALOG_SIZE.height;
        return bounds;
    }

    public void processOutgoingMsgs(List<OutgoingMsg> msgs) {
        if (msgs != null) {
            for (OutgoingMsg msg : msgs) {
                if (msg.getWriteTo().equals(OutgoingMsg.WRITE_TO_MSG)) {
                    append(msg.getContent());
                } else if (msg.getWriteTo().equals(OutgoingMsg.WRITE_TO_TOPIC)) {
                    this.topicCache = msg.getContent();
                } else if (msg.getWriteTo().equals(
                        OutgoingMsg.REFRESH_TOPIC_NOTICE)) {
                    append("标题更新: " + topicCache);
                }
            }
        }
    }

    private void append(String msg) {
        taLog.append(Colors.removeFormattingAndColors(msg) + "\r\n");
    }
}

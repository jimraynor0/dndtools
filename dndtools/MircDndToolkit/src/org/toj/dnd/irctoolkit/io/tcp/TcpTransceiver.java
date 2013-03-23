package org.toj.dnd.irctoolkit.io.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.Command;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class TcpTransceiver extends Thread {
    private static TcpTransceiver INSTANCE = new TcpTransceiver();

    public static TcpTransceiver getInstance() {
        return INSTANCE;
    }

    private Logger log = Logger.getLogger(this.getClass());

    private ServerSocket serverSocket;

    private Socket clientSocket;

    private PrintWriter out;

    private BufferedReader in;

    private TcpTransceiver() {
        this.setDaemon(true);
        try {
            serverSocket = new ServerSocket(50811);
            log.info("listening on port 50811");
        } catch (IOException e) {
            log.error("could not listen on port: 50811.");
            System.exit(1);
        }
    }

    private void listen() {
        log.debug("waiting for client to connect...");
        try {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            log.info("client accepted");
        } catch (IOException e) {
            log.error("create connection failed.");
            try {
                clear();
            } catch (IOException e1) {
                // ignore
            }
        }
    }

    private void clear() throws IOException {
        if (this.out != null) {
            this.out.close();
            this.out = null;
        }
        if (this.in != null) {
            this.in.close();
            this.in = null;
        }
        if (this.clientSocket != null) {
            this.clientSocket.close();
            this.clientSocket = null;
        }
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                if (!connAlive()) {
                    log.debug("connection not alive, (re-)establishing...");
                    listen();
                    if (!connAlive()) {
                        continue;
                    }
                }
                String cmd = in.readLine();
                log.debug("Command received: " + cmd);
                try {
                    Command c = IrcCommandFactory.buildCommand(cmd, null, 0);
                    if (c == null) {
                        log.warn("Command dropped: invalid command.");
                    } else {
                        ToolkitEngine.getEngine().queueCommand(c);
                    }
                } catch (Exception e) {
                    log.warn("Exception while parsing cmd: ", e);
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private boolean connAlive() {
        return this.clientSocket != null && clientSocket.isConnected();
    }

    public void send(String content, String chan, String caller, String writeTo)
            throws IOException {
        StringBuilder msg = new StringBuilder(writeTo).append(" ").append(chan)
                .append(" ").append(caller);
        if (content != null && !content.isEmpty()) {
            msg.append(" ").append(content);
        }
        // msg.append("\r\n");
        this.out.write(msg.toString() + "\r\n");
        this.out.flush();

        log.debug("line sent: " + msg.toString());
    }

    public void send(List<OutgoingMsg> msgs) {
        for (OutgoingMsg msg : msgs) {
            try {
                send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void send(OutgoingMsg msg) throws IOException {
        send(msg.getContent(), msg.getChan(), msg.getCaller(), msg.getWriteTo());
    }

    public void close() throws IOException {
        clear();
        serverSocket.close();
    }
}

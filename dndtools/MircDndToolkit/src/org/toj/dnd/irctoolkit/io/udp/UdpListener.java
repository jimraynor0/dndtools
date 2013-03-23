package org.toj.dnd.irctoolkit.io.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.Command;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;

public class UdpListener extends Thread {

    private Logger log = Logger.getLogger(this.getClass());

    private static final int LISTENING_PORT = 20811;

    private DatagramSocket serverSocket;

    public UdpListener() {
        try {
            serverSocket = new DatagramSocket(LISTENING_PORT);
        } catch (SocketException e) {
            System.err.println("Exception: couldn't create datagram socket");
            e.printStackTrace();
            System.exit(1);
        }
        this.setDaemon(true);

        UdpTransmitter.init(serverSocket);
        log.info("UdpConnection up and running...");
    }

    public void run() {
        while (true) {
            try {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                serverSocket.receive(packet);

                String raw = new String(data);
                String cmd = raw.substring(0, raw.indexOf(0));

                log.debug("Command received: " + cmd);
                try {
                    Command c = IrcCommandFactory.buildCommand(cmd,
                            packet.getAddress(), packet.getPort());
                    if (c == null) {
                        log.warn("Command dropped: invalid command.");
                    } else {
                        ToolkitEngine.getEngine().queueCommand(c);
                    }
                } catch (Exception e) {
                    log.warn("Exception while parsing cmd: ", e);
                }
            } catch (IOException e) {
                System.err.println("Exception: error during receiving packet.");
                e.printStackTrace();
            }
        }
    }
}

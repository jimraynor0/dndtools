package org.toj.dnd.irctoolkit.io.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.apache.log4j.Logger;

public class UdpTransmitter {
    private Logger log = Logger.getLogger(this.getClass());

    private static UdpTransmitter INSTANCE;
    private DatagramSocket socket;

    public static UdpTransmitter getInstance() {
        return INSTANCE;
    }

    public static void init(DatagramSocket serverSocket) {
        INSTANCE = new UdpTransmitter(serverSocket);
    }

    private UdpTransmitter(DatagramSocket serverSocket) {
        this.socket = serverSocket;
    }

    public void send(String content, String chan, String caller,
            String writeTo, InetAddress destAddr, int destPort)
            throws IOException {
        StringBuilder msg = new StringBuilder(writeTo).append(" ").append(chan)
                .append(" ").append(caller);
        if (content != null && !content.isEmpty()) {
            msg.append(" ").append(content);
        }
        DatagramPacket packet = new DatagramPacket(msg.toString().getBytes(),
                msg.toString().getBytes().length, destAddr, destPort);
        socket.send(packet);
        log.debug("Packet sent: " + msg.toString());
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
        send(msg.getContent(), msg.getChan(), msg.getCaller(),
                msg.getWriteTo(), msg.getDestAddr(), msg.getDestPort());
    }
}

package org.toj.dnd.irctoolkit.io.udp;

import java.net.InetAddress;

public class OutgoingMsg {

    public static final String WRITE_TO_TOPIC = "topic";
    public static final String WRITE_TO_MSG = "msg";
    public static final String REFRESH_TOPIC_NOTICE = "updateTopic";

    private String content;
    private String writeTo;
    private String chan;
    private String caller;
    private InetAddress destAddr;
    private int destPort;

    public OutgoingMsg(String chan, String caller, String content,
            String writeTo, InetAddress incomingAddr, int incomingPort) {
        this.chan = chan;
        this.caller = caller;
        this.content = content;
        this.writeTo = writeTo;
        this.destAddr = incomingAddr;
        this.destPort = incomingPort;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriteTo() {
        return writeTo;
    }

    public void setWriteTo(String writeTo) {
        this.writeTo = writeTo;
    }

    public String getChan() {
        return chan;
    }

    public void setChan(String chan) {
        this.chan = chan;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public InetAddress getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(InetAddress destAddr) {
        this.destAddr = destAddr;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
}

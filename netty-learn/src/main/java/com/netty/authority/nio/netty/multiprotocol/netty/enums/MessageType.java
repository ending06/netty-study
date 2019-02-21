package com.netty.authority.nio.netty.multiprotocol.netty.enums;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public enum MessageType {

    SERVICE_REQ((byte) 0), //

    SERVICE_RESP((byte) 1), //

    ONE_WAY((byte) 2), //

    LOGIN_REQ((byte) 3), //

    LOGIN_RESP((byte) 4), //

    HEARTBEAT_REQ((byte) 5), //

    HEARTBEAT_RESP((byte) 6);//

    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
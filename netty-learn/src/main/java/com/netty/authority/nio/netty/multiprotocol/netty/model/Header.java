package com.netty.authority.nio.netty.multiprotocol.netty.model;

import java.util.HashMap;
import java.util.Map;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/20<p>
// -------------------------------------------------------

public class Header {

    private int crcCode = 0xabef0101;

    /**
     * 消息长度
     * */
    private int length;

    /**
     * 会话ID
     * */
    private long sessionID;

    /**
     * 消息类型
     * */
    private byte type;
    /**
     * 消息优先级
     * */
    private byte priority;

    private Map<String, Object> attchment = new HashMap<String, Object>();

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttchment() {
        return attchment;
    }

    public void setAttchment(Map<String, Object> attchment) {
        this.attchment = attchment;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Header{");
        sb.append("crcCode=").append(crcCode);
        sb.append(", length=").append(length);
        sb.append(", sessionID=").append(sessionID);
        sb.append(", type=").append(type);
        sb.append(", priority=").append(priority);
        sb.append(", attchment=").append(attchment);
        sb.append('}');
        return sb.toString();
    }
}
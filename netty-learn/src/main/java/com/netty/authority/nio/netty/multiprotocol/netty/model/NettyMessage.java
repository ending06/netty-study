package com.netty.authority.nio.netty.multiprotocol.netty.model;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/20<p>
// -------------------------------------------------------

public class NettyMessage {
    /**
     * 消息头
     * */
    private Header header;

    /**
     * 消息体
     * */
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NettyMessage{");
        sb.append("header=").append(header);
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
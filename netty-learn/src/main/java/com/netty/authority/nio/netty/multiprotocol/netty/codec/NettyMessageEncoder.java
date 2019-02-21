package com.netty.authority.nio.netty.multiprotocol.netty.codec;

import com.netty.authority.nio.netty.multiprotocol.netty.model.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/20<p>
// -------------------------------------------------------

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {

        if (msg == null || msg.getHeader() == null) {
            throw new Exception("the encode message is null");
        }
        sendBuf.writeInt(msg.getHeader().getCrcCode());

        sendBuf.writeInt(msg.getHeader().getLength());

        sendBuf.writeLong(msg.getHeader().getSessionID());

        sendBuf.writeByte(msg.getHeader().getType());

        sendBuf.writeByte(msg.getHeader().getPriority());

        sendBuf.writeInt(msg.getHeader().getAttchment().size());

        String key = null;

        byte[] keyArray = null;

        Object value = null;

        for (Map.Entry<String, Object> param : msg.getHeader().getAttchment().entrySet()) {

            key = param.getKey();

            keyArray = key.getBytes("UTF-8");

            sendBuf.writeInt(keyArray.length);

            sendBuf.writeBytes(keyArray);

            value = param.getValue();

            marshallingEncoder.encode(value, sendBuf);
        }

        key = null;

        keyArray = null;

        value = null;

        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);
    }
}
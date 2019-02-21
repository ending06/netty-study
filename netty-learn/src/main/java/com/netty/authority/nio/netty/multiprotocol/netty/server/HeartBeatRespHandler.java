package com.netty.authority.nio.netty.multiprotocol.netty.server;

import com.netty.authority.nio.netty.multiprotocol.netty.enums.MessageType;
import com.netty.authority.nio.netty.multiprotocol.netty.model.Header;
import com.netty.authority.nio.netty.multiprotocol.netty.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        NettyMessage message = (NettyMessage) msg;

        // 返回心跳应答消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {

            System.out.println("Receive client heart beat message:----->" + message);

            NettyMessage heartBeat = buildHeartBeat();

            System.out.println("Send heart beat response message to Client:----->" + heartBeat);

            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {

        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());

        NettyMessage message = new NettyMessage();
        message.setHeader(header);
        return message;
    }
}
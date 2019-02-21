package com.netty.authority.nio.netty.multiprotocol.netty.client;

import com.netty.authority.nio.netty.multiprotocol.netty.enums.MessageType;
import com.netty.authority.nio.netty.multiprotocol.netty.model.Header;
import com.netty.authority.nio.netty.multiprotocol.netty.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class HeartBeatReqHandler extends ChannelHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        NettyMessage message = new NettyMessage();

        // 握手成功，主动发送心跳消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {

            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000,
                    TimeUnit.MILLISECONDS);
        } else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            System.out.println("Client receive server heart beat message:----->" + message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {

            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Client send heart beat message to server:---->" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat() {

            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());

            NettyMessage message = new NettyMessage();
            message.setHeader(header);

            return message;
        }
    }

}
package com.netty.authority.nio.netty.multiprotocol.netty.client;

import com.netty.authority.nio.netty.multiprotocol.netty.enums.MessageType;
import com.netty.authority.nio.netty.multiprotocol.netty.model.Header;
import com.netty.authority.nio.netty.multiprotocol.netty.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(buildLogReq(ctx));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手应答消息，需要判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {

            byte loginResult = (byte) message.getBody();

            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                ctx.close();
            } else {
                System.out.println("login is OK:" + message);
                ctx.fireChannelRead(message);
            }
        } else {
            ctx.fireChannelRead(message);
        }
    }

    private Object buildLogReq(ChannelHandlerContext ctx) {

        NettyMessage nettyMessage = new NettyMessage();

        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());

        nettyMessage.setHeader(header);

        return nettyMessage;
    }
}
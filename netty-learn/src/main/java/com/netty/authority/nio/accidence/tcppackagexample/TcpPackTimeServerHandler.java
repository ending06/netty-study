package com.netty.authority.nio.accidence.tcppackagexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/13<p>
// -------------------------------------------------------

public class TcpPackTimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] req = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(req);

        String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());

        System.out.println("the time server receive order:" + body + ";the counter is :" + (++counter));

        String currentTime = "query time".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
                : "BAD REQ";
        currentTime = currentTime + System.getProperty("line.separator");

        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) {
        ex.printStackTrace();
        ctx.close();
    }
}
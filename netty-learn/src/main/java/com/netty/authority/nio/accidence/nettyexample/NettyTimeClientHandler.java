package com.netty.authority.nio.accidence.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class NettyTimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMessage;

    public NettyTimeClientHandler() {
        byte[] req = "query".getBytes();

        // 堆缓存
        firstMessage = Unpooled.buffer(req.length);

        firstMessage.writeBytes(req);
    }

    /**
     * 当客户端和服务端链接成功后，netty的NIO线程会调用channelActive方法，发送查询时间的指令给服务器
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 将请求消息发送服务端
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端响应时，客户端的channelRead方法被调用，从byteBuf中读取消息，并打印输出
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] req = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(req);

        String body = new String(req, "UTF-8");

        System.out.println("Now is:" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        // 释放资源
        throwable.printStackTrace();
        ctx.close();
    }
}
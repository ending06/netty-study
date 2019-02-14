package com.netty.authority.nio.accidence.decoderexample.delimiterbasedframedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class DelimiterEchoServerHandler extends ChannelHandlerAdapter {

    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        String body = (String) msg;

        System.out.println("this is " + (++counter) + "times receive from client:" + body);

        // 由于delimiter解码器过滤了分隔符，此时服务端收到的是解码后的字符串对象，故返回给客户端的时候，需要在请求消息尾部拼接"$_"
        body += "$_";

        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());

        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();

        ctx.close();
    }
}
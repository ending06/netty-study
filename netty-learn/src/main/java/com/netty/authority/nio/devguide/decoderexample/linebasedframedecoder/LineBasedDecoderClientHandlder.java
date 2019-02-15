package com.netty.authority.nio.devguide.decoderexample.linebasedframedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class LineBasedDecoderClientHandlder extends ChannelHandlerAdapter {

    private int counter;

    private byte[] request;

    public LineBasedDecoderClientHandlder() {
        request = ("query time" + System.getProperty("line.separator")).getBytes();
    }

    /**
     * 当客户端和服务端链接成功后，netty的NIO线程会调用channelActive方法，发送查询时间的指令给服务器
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf message = null;

        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(request.length);

            message.writeBytes(request);

            ctx.writeAndFlush(message);
        }
    }

    /**
     * 当服务端响应时，客户端的channelRead方法被调用，从byteBuf中读取消息，并打印输出
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        String body = (String) msg;

        System.out.println("Now is :" + body + "; the counter is:" + (++counter));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
        ctx.close();
    }
}
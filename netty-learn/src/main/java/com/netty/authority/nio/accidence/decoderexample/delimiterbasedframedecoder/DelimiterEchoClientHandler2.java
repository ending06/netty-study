package com.netty.authority.nio.accidence.decoderexample.delimiterbasedframedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class DelimiterEchoClientHandler2 extends ChannelHandlerAdapter {

    private int counter;

    private static final String ECHO_REQ = "hi.test_2.$_";

    public DelimiterEchoClientHandler2() {
    }

    /**
     * 当客户端和服务端链接成功后，netty的NIO线程会调用channelActive方法，发送查询时间的指令给服务器
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 100; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(ECHO_REQ.getBytes());

            ctx.writeAndFlush(byteBuf);
        }
    }

    /**
     * 收到服务器的响应，将其打印出来
     * */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("this is " + (++counter) + "times;receive from server: " + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
        ctx.close();
    }

}
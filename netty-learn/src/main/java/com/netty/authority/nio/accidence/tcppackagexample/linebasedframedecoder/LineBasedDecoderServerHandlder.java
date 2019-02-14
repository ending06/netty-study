package com.netty.authority.nio.accidence.tcppackagexample.linebasedframedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class LineBasedDecoderServerHandlder extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        String body = (String) msg;

        System.out.println("the time server receive order:" + body + ";the counter is:" + (++counter));

        String currentTime = "query time".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
                : "BAD REQ";

        currentTime = currentTime+System.getProperty("line.separator");

        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {

        throwable.printStackTrace();

        ctx.close();
    }
}
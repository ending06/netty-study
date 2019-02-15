package com.netty.authority.nio.devguide.decoderexample.fixedlengthframedecoder;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class FixedLengthEchoServerHandler extends ChannelHandlerAdapter {

    /**
     * 通过telnet直接测试:
     * ~ $telnet 127.0.0.1 8080
     Trying 127.0.0.1...
     Connected to localhost.
     Escape character is '^]'.
     this is my first test,fixedLengthFrameDecoder netty code,netty learn
     -------------------------------------
     receive client:this is my first tes
     receive client:t,fixedLengthFrameDe
     receive client:coder netty code,net
     -------------------------------------
     由于设置的是20字符长度，故会以20字符输出一次
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("receive client:" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();

        ctx.close();
    }
}
package com.netty.server.demo.spdy;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/20<p>
// -------------------------------------------------------

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class DefaultSpdyOrHttpChooser implements ChannelHandler {
    public DefaultSpdyOrHttpChooser(int i, int i1) {
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
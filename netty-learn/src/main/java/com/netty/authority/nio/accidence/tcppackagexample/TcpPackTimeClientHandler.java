package com.netty.authority.nio.accidence.tcppackagexample;

import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/13<p>
// -------------------------------------------------------

public class TcpPackTimeClientHandler extends ChannelHandlerAdapter {

    private int counter;

    private byte[] req;

    public TcpPackTimeClientHandler() {
        req = ("query time" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf message = null;

        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);

            message.writeBytes(req);

            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] req = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(req);

        String body = new String(req, "UTF-8");

        System.out.println("Now is:" + body + "; the counter is:" + (++counter));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
        ctx.close();
    }
}
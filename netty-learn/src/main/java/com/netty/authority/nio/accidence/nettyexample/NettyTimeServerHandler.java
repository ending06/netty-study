package com.netty.authority.nio.accidence.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// 对网络事件进行读写操作
// -------------------------------------------------------

public class NettyTimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        // 类似JDK NIO中的byteBuffer
        ByteBuf byteBuffer = (ByteBuf) msg;

        // readableBytes()可以获取缓冲区可读的字节数
        byte[] req = new byte[byteBuffer.readableBytes()];

        // 将缓冲区中字节数组复制到新建的req数组中
        byteBuffer.readBytes(req);

        String body = new String(req, "UTF-8");

        System.out.println("the netty time server receive order:" + body);

        String currentTime = "query".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
                : "BAD ERROR";

        System.out.println("the netty time server currentTime:" + currentTime);

        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());

        // 异步发送应答消息给客户端
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将消息发送队列中的消息写入到socketChannel中，发送给对方
        // 为了防止频繁的唤醒selector进行消息发送，Netty的write方法不是直接将消息写入到SocketChannel中，而只是把待发送的消息放到发送缓冲数组中，再调用flush方法，将消息全部写入到SocketChannel中
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
        ctx.close();
    }
}
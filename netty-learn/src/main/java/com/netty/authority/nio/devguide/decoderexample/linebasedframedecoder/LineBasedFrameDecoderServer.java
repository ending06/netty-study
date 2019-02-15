package com.netty.authority.nio.devguide.decoderexample.linebasedframedecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class LineBasedFrameDecoderServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new LineBasedFrameDecoderServer().bind(port);
    }

    private void bind(int port) throws InterruptedException {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .option(ChannelOption.SO_BACKLOG, 1024)//
                    .childHandler(new ChildChannelHander());

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private class ChildChannelHander extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 依次遍历bytebuf中的可读字节，遇到\n \r\n，则认为是结束，从可读位置到结束位置区间的字节组成一行，即是一个以换行符作为结束标志的解码器
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));

            // 将收到的对象转换为字符串，
            ch.pipeline().addLast(new StringDecoder());

            // 如上两个解码器组合：按行切换的文本解码器，之后继续调用业务处理过程的handler
            ch.pipeline().addLast(new LineBasedDecoderServerHandlder());
        }
    }
}
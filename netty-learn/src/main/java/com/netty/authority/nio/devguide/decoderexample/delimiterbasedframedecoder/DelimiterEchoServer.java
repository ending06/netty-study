package com.netty.authority.nio.devguide.decoderexample.delimiterbasedframedecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// 特殊分隔符解码器demo
// -------------------------------------------------------

public class DelimiterEchoServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new DelimiterEchoServer().bind(port);
    }

    private void bind(int port) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .option(ChannelOption.SO_BACKLOG, 1024)//
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            // 分隔符缓存对象
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());

                            // param1：表示单条消息的最大长度是1024，当达到该长度仍未找到分隔符，则抛出异常，防止由于异常码流失缺失分隔符导致的内存溢出
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));

                            ch.pipeline().addLast(new StringDecoder());

                            ch.pipeline().addLast(new DelimiterEchoServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
package com.netty.authority.nio.accidence.decoderexample.fixedlengthframedecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// 固定长度解码器demo
// -------------------------------------------------------

public class FixedLengthEchoServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new FixedLengthEchoServer().bind(port);
    }

    private void bind(int port) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .option(ChannelOption.SO_BACKLOG, 1024)//
                    .handler(new LoggingHandler(LogLevel.INFO))//
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));

                            ch.pipeline().addLast(new StringDecoder());

                            ch.pipeline().addLast(new FixedLengthEchoServerHandler());
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
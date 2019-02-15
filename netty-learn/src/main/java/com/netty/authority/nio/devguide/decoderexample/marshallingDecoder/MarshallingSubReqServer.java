package com.netty.authority.nio.devguide.decoderexample.marshallingDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/15<p>
// -------------------------------------------------------

public class MarshallingSubReqServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new MarshallingSubReqServer().bind(port);
    }

    private void bind(int port) throws InterruptedException {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)//
                    .option(ChannelOption.SO_BACKLOG, 100)//
                    .handler(new LoggingHandler(LogLevel.INFO))//
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());

                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());

                            ch.pipeline().addLast(new MarshallingSubReqServerHanlder());
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
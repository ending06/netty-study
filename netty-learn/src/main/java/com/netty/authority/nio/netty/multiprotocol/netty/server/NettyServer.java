package com.netty.authority.nio.netty.multiprotocol.netty.server;

import com.netty.authority.nio.netty.multiprotocol.netty.codec.NettyMessageDecoder;
import com.netty.authority.nio.netty.multiprotocol.netty.constant.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class NettyServer {

    private void bind() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)//
                .channel(NioServerSocketChannel.class)//
                .option(ChannelOption.SO_BACKLOG, 100)//
                .handler(new LoggingHandler(LogLevel.INFO))//
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));

                        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));

                        ch.pipeline().addLast(new LoginAuthRespHandler());

                        ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                    }
                });

        serverBootstrap.bind(NettyConstant.REMOTE_IP, NettyConstant.PORT).sync();

        System.out.println("Netty server start ok:" + (NettyConstant.REMOTE_IP + " : " + NettyConstant.PORT));

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind();
    }
}
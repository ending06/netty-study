package com.netty.authority.nio.accidence.tcppackagexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/13<p>
// -------------------------------------------------------

public class TcpPackTimeServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new TcpPackTimeServer().bind(port);
    }

    private void bind(int port) throws InterruptedException {

        // 接收服务器连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // socketChannel网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // netty启动NIO服务器端辅助辅助类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .option(ChannelOption.SO_BACKLOG, 2014)//
                    .childHandler(new ChildChannelHandler());// 处理网络事件

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TcpPackTimeServerHandler());
        }
    }
}
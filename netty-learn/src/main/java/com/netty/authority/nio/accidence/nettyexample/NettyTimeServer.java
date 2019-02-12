package com.netty.authority.nio.accidence.nettyexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class NettyTimeServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new NettyTimeServer().bind(port);
    }

    public void bind(int port) throws InterruptedException {

        // NioEventLoopGroup线程组，包含一组NIO线程，专用于网络事件的处理，实际上它们即是Reactor线程组
        // 用户服务端接收客户端的链接
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 用于socketChannel的网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)//
                    .channel(NioServerSocketChannel.class)// 功能类似于jdk NIO类库中的severSocketChannel类
                    .option(ChannelOption.SO_BACKLOG, 1024)//
                    .childHandler(new ChildChannelHandler());// 功能类似于Reactor模式中的handler类，处理网络事件，如记录日志，消息解码等

            // 绑定端口，同步等待成功 同步阻塞方法sync等待绑定操作完成
            // 绑定完成之后，netty会返回一个ChannelFuture，用于异步操作的通知回调
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 方法阻塞，等待服务器监听端口链路关闭之后，main函数才退出
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new NettyTimeServerHandler());
        }
    }
}
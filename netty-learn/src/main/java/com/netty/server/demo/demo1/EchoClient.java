package com.netty.server.demo.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/10/12</p>
// 客户端连接后，它发送消息：“hello i come！”
// 服务器输出接收到消息并将其返回给客户端
// 客户输出接收到的消息并退出
// -------------------------------------------------------

public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();// 处理客户端事件
        try {
            Bootstrap bootstrap = new Bootstrap(); //
            bootstrap.group(group) // 2
                    .channel(NioSocketChannel.class) // 3
                    .remoteAddress(new InetSocketAddress(host, port)) // 设置服务器的地址
                    .handler(new ChannelInitializer<SocketChannel>() { // 5
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new EchoClientHandler());
                                }
                            });

            ChannelFuture channelFuture = bootstrap.connect().sync(); // 6

            channelFuture.channel().closeFuture().sync(); // 7
        } finally {
            group.shutdownGracefully().sync(); // 8
        }
    }

    public static void main(String[] args) throws Exception {
        final String host = "127.0.0.1";
        final int port = Integer.parseInt("8080");

        new EchoClient(host, port).start();
    }
}
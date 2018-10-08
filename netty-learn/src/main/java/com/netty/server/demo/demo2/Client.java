package com.netty.server.demo.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * --------------------- Change Logs----------------------
 * <p>@author ruirui.qu Initial Created at 18/10/8<p>
 * -------------------------------------------------------
 **/
public class Client {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        final int port = 8765;

        final String serverIp = "127.0.0.1";

        bootstrap
                .group(group)//
                .channel(NioSocketChannel.class)//
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                ch.pipeline().addLast(new ClientHandler(port));

            }
        });

        ChannelFuture channelFuture = bootstrap.connect(serverIp,port).sync();

        channelFuture.channel().closeFuture().sync();

        group.shutdownGracefully();
    }
}
package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/9/27<p>
// -------------------------------------------------------
public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage" + HttpServer.class.getSimpleName() + "port");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new HttpServer(port).start();
    }

    private void start() {
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        b.group(group).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("initChannel ch:"+ch);
                ch.pipeline().addLast("decoder",new HttpRequestDecoder())
                        .addLast("encoder",new HttpRequestEncoder())
                        .addLast("aggregator",new HttpObjectAggregator(512*1024))
                        .addLast("handler",new HttpHandler());

            }
        }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

    }
}

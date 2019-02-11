package com.netty.server.demo.spdy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/20<p>
// -------------------------------------------------------

public class SpdyServer {

    private final NioEventLoopGroup group = new NioEventLoopGroup(); // 1
    private final SslContext context;
    private Channel channel;

    public SpdyServer(SslContext context) { // 2
        this.context = context;
    }

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap(); // 3
        bootstrap.group(group).channel(NioServerSocketChannel.class).childHandler(new SpdyChannelInitializer(context)); // 4
        ChannelFuture future = bootstrap.bind(address); // 5
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    public void destroy() { // 6
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Please give port as argument");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContext.newServerContext(cert.certificate(), cert.privateKey()); // 7
        final SpdyServer endpoint = new SpdyServer(context);
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}
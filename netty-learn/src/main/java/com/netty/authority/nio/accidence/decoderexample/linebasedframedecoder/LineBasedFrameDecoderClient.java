package com.netty.authority.nio.accidence.decoderexample.linebasedframedecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class LineBasedFrameDecoderClient {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new LineBasedFrameDecoderClient().connect(port, "127.0.0.1");
    }

    private void connect(int port, String host) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group)//
                    .channel(NioSocketChannel.class)//
                    .option(ChannelOption.TCP_NODELAY, true)//
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new LineBasedDecoderClientHandlder());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
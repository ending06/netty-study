package com.netty.authority.nio.netty.multiprotocol.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/19<p>
// -------------------------------------------------------

public class WebSocketServer {

    private void run(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup wokerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, wokerGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .handler(new LoggingHandler(LogLevel.INFO))//
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("handler", new WebSocketServerHandler());
                        }
                    });

            // channelFuture??
            Channel channel = serverBootstrap.bind(port).sync().channel();

            System.out.println("web socket server started at port:" + port + ".");

            System.out.println("open your browser and navigate to http://localhost:" + port + "/");

            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            wokerGroup.shutdownGracefully();
        }

    }

    /**
     * 运行run   浏览器输入：file:///Users/quruirui/ruirui.qu/pay/pay_learn/source_code/netty_source/netty-study/netty-learn/src/main/webapp/WebSocketServer.html
     * */
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;

        new WebSocketServer().run(port);
 }
}
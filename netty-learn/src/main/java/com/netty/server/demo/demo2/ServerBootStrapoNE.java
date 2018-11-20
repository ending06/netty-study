package com.netty.server.demo.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/16<p>
// -------------------------------------------------------

public class ServerBootStrapOne {

    /**
     * 新的 EventLoop 会创建一个新的 Thread。出于该原因，EventLoop 实例应该尽量重用。或者限制实例的数量来避免耗尽系统资源
     * */
    public void start() {

        ServerBootstrap bootstrap = new ServerBootstrap(); // 1:创建新的ServerBootStrap来创建新的SocketChannel管道并绑定

        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())// 2EvenetLoopGroups从SeverChannel和接收到管道来注册并获取EventLoops

                .channel(NioServerSocketChannel.class) // 3 指定channel类型

                .childHandler( // 4 //设置处理器用户接收收到的管道的I/O和数据
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture connectFuture;

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                Bootstrap bootstrap = new Bootstrap();// 5 创建一个新的BootStrap来链接到远程主机
                                bootstrap.channel(NioSocketChannel.class) // 6 //设置管道类
                                        .handler(new SimpleChannelInboundHandler<ByteBuf>() { // 7 //设置处理器来处理I/O
                                                    @Override
                                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in)
                                                            throws Exception {
                                                        System.out.println("Reveived data");
                                                    }
                                                });
                                bootstrap.group(ctx.channel().eventLoop()); // 8 使用相同的EventLoop作为分配接收的管道

                                connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80)); // 9
                                                                                                                 // 链接到远端
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
                                    throws Exception {
                                if (connectFuture.isDone()) {
                                    // do something with the data //10 链接完成处理业务逻辑
                                }
                            }
                        });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080)); // 11 通过配置了BootStrap来绑定管道

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bound attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
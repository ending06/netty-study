package com.netty.authority.nio.netty.multiprotocol.netty.client;

import com.netty.authority.nio.netty.multiprotocol.netty.codec.NettyMessageDecoder;
import com.netty.authority.nio.netty.multiprotocol.netty.codec.NettyMessageEncoder;
import com.netty.authority.nio.netty.multiprotocol.netty.constant.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    private void connect(int port, String remoteIp) throws InterruptedException {

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)//
                    .channel(NioSocketChannel.class)//
                    .option(ChannelOption.TCP_NODELAY, true)//
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));

                            ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());

                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));

                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());

                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                        }
                    });

            // 发起异步链接操作
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(remoteIp, port),
                    new InetSocketAddress(NettyConstant.LOCAL_IP, NettyConstant.LOCAL_PORT)).sync();

            // 当对应的channel关闭时，就会返回对应的channel

            channelFuture.channel().closeFuture().sync();
        } finally {
            // 所有资源释放后，清空资源，重新链接
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);

                        try {
                            // 重新发起链接操作
                            connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
                        } catch (InterruptedException e) {
                            System.out.println("finallly.connect.exception:" + e);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("finally.sleep.exception:" + e);
                    }
                }
            });
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
    }
}
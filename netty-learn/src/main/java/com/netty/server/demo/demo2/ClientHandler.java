package com.netty.server.demo.demo2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * --------------------- Change Logs----------------------
 * <p>@author ruirui.qu Initial Created at 18/10/8<p>
 * -------------------------------------------------------
 **/
public class ClientHandler extends ChannelHandlerAdapter {

    private static final String SUCCESS = "OK";

    private String ip;
    private int port;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    public ClientHandler(int port) {
        this.port = port;

        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // 通道建立初始化时 发送信息 准备握手验证
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String authInfo = this.ip + ":" + this.port;

        ctx.writeAndFlush(authInfo);
    }

    //当服务器发送认证信息后,开始启动心跳发送
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof String){

            //认证成功
            if(SUCCESS.equals((String)msg)){

                this.scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new HeartTask(ctx,ip,port),2,3, TimeUnit.SECONDS);

            }else{

                System.out.println("服务器发来消息：" + msg);
            }

        }

        ReferenceCountUtil.release(msg);

    }

    // 如果出现异常 取消定时
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
        }

    }

}
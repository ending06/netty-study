package com.netty.server.demo.demo2;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//--------------------- Change Logs----------------------
//  <p>@author ruirui.qu Initial Created at 18/10/9</p>
//-------------------------------------------------------

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String,HeartInfo> heartInfoMap = new HashMap<String, HeartInfo>();

    private static final List<String> authList = new ArrayList<String>();

    static {
        //从其他地方加载出来的IP列表
        authList.add("127.0.0.1:8765");
    }

    //服务器会接收到2种消息 一个是客户端初始化时发送过来的认证信息 第二个是心跳信息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof String){

            if(authList.contains(msg)){ //验证通过
                ctx.writeAndFlush("OK");
            }else{
                ctx.writeAndFlush("不在认证列表中...");
            }

        }else if(msg instanceof HeartInfo){

            System.out.println((HeartInfo)msg);

            ctx.writeAndFlush("心跳接收成功!");

            HeartInfo heartInfo = (HeartInfo)msg;
            heartInfoMap.put(heartInfo.getIp() + ":" + heartInfo.getPort(),heartInfo);
        }

    }
}
package com.netty.authority.nio.netty.multiprotocol.netty.server;

import com.netty.authority.nio.netty.multiprotocol.netty.enums.MessageType;
import com.netty.authority.nio.netty.multiprotocol.netty.model.Header;
import com.netty.authority.nio.netty.multiprotocol.netty.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();

    private String[] whiteList = { "127.0.0.1" };

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        NettyMessage message = (NettyMessage) msg;

        // 登陆请求
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();

            NettyMessage loginResponse = null;

            // 重复登陆，拒绝
            if (nodeIndex.contains(nodeIndex)) {
                loginResponse = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

                String ip = address.getAddress().getHostAddress();

                boolean isOK = false;

                for (String WIP : whiteList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }

                loginResponse = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);

                if (isOK) {
                    nodeCheck.put(nodeIndex, true);
                }
                System.out.println("the login response is:" + loginResponse + ";body[" + loginResponse.getBody() + "]");

                ctx.writeAndFlush(loginResponse);
            }

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage nettyMessage = new NettyMessage();

        Header header = new Header();

        header.setType(MessageType.LOGIN_RESP.value());

        nettyMessage.setHeader(header);

        nettyMessage.setBody(result);

        return nettyMessage;
    }
}
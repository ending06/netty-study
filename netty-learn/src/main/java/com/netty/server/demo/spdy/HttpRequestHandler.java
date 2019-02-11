package com.netty.server.demo.spdy;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/20<p>
// -------------------------------------------------------

/**
 * 当前注解:说明ChannelHandler是否可以在多个channel直接共享使用
 * */
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception { // 1
        if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continue(ctx); // 2
        }

        FullHttpResponse response = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK); // 3
        response.content().writeBytes(getContent().getBytes(CharsetUtil.UTF_8)); // 4
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8"); // 5

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        if (keepAlive) { // 6
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ChannelFuture future = ctx.writeAndFlush(response); // 7

        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE); // 8
        }
    }

    protected String getContent() { // 9
        return "This content is transmitted via HTTP\r\n";
    }

    private static void send100Continue(ChannelHandlerContext ctx) { // 10
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // 11
        cause.printStackTrace();
        ctx.close();
    }
}

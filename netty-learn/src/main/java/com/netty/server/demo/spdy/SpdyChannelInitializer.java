package com.netty.server.demo.spdy;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/20<p>
// -------------------------------------------------------

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import org.eclipse.jetty.npn.NextProtoNego;

import javax.net.ssl.SSLEngine;

public class SpdyChannelInitializer extends ChannelInitializer<SocketChannel> { // 1
    private final SslContext context;

    public SpdyChannelInitializer(SslContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc()); // 3
        engine.setUseClientMode(false); // 4

        NextProtoNego.put(engine, new DefaultServerProvider()); // 5
        NextProtoNego.debug = true;

        pipeline.addLast("sslHandler", new SslHandler(engine)); // 6
        pipeline.addLast("chooser", new DefaultSpdyOrHttpChooser(1024 * 1024, 1024 * 1024));
    }
}

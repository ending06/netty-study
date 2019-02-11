package com.netty.server.demo.spdy;

import io.netty.channel.ChannelHandler;

//--------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/20<p>
//-------------------------------------------------------
@ChannelHandler.Sharable
public class SpdyRequestHandler extends HttpRequestHandler {   //1
    @Override
    protected String getContent() {
        return "This content is transmitted via SPDY\r\n";  //2
    }
}
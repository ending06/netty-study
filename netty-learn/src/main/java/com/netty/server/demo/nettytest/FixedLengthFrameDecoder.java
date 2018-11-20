package com.netty.server.demo.nettytest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/16</p>
// -------------------------------------------------------

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be positive integer" + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= frameLength) {
            ByteBuf byteBuf = in.readBytes(frameLength);
            out.add(byteBuf);
        }
    }
}
package com.netty.server.demo.nettytest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/11/16<p>
// -------------------------------------------------------

public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i=0;i<9;i++){
            byteBuf.writeByte(i);
        }

        ByteBuf input = byteBuf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        Assert.assertFalse(channel.writeInbound(input.readBytes(2)));
        Assert.assertTrue(channel.writeInbound(input.readBytes(7)));
        Assert.assertTrue(channel.finish());

        ByteBuf read = (ByteBuf)channel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(3),read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(3), read);
        read.release();

        Assert.assertNull(channel.readInbound());
        byteBuf.release();
    }
}
package com.netty.authority.nio.netty.multiprotocol.netty.codec;

import com.netty.authority.nio.netty.multiprotocol.netty.model.Header;
import com.netty.authority.nio.netty.multiprotocol.netty.model.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {

        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);

        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        if (frame == null) {

            System.out.println("NettyMessageDecoder.decode.frame is null");
            return null;
        }

        NettyMessage message = new NettyMessage();

        // 按照顺序取值===>看encoder的顺序
        Header header = new Header();

        header.setCrcCode(frame.readInt());

        header.setLength(frame.readInt());

        header.setSessionID(frame.readLong());

        header.setType(frame.readByte());

        header.setPriority(frame.readByte());

        int size = frame.readInt();

        if (size > 0) {
            Map<String, Object> attch = new HashMap<>();

            int keySize = 0;

            byte[] keyArray = null;

            String key = null;

            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();

                keyArray = new byte[keySize];

                frame.readBytes(keyArray);

                key = new String(keyArray, "UTF-8");

                attch.put(key, marshallingDecoder.decode(frame));
            }

            keyArray = null;

            key = null;

            header.setAttchment(attch);
        }

        if (frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }

        message.setHeader(header);

        return message;
    }
}
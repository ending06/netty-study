package com.netty.authority.nio.netty.multiprotocol.netty.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/21<p>
// -------------------------------------------------------

public class MarshallingDecoder {

    private Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception {
        int objectSize = in.readInt();

        ByteBuf buf = in.slice(in.readerIndex(), objectSize);

        ByteInput input = new ChannelBufferByteInput(buf);

        try {
            unmarshaller.start(input);

            Object obj = unmarshaller.readObject();

            unmarshaller.finish();

            in.readerIndex(in.readerIndex() + objectSize);

            return obj;

        } finally {
            unmarshaller.close();
        }
    }
}
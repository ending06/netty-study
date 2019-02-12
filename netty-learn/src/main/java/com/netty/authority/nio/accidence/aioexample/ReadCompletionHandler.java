package com.netty.authority.nio.accidence.aioexample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if (channel != null) {
            this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

        attachment.flip();

        byte[] body = new byte[attachment.remaining()];

        attachment.get(body);

        try {
            String req = new String(body, "UTF-8");
            System.out.println("the time server receive order:" + req);

            String currentTime = "QUERYTIMEORDER".equalsIgnoreCase(req) ? new Date(System.currentTimeMillis())
                    .toString() : "BAD REQ";

            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            System.out.println("completed.UnsupportedEncodingException.exception:" + e);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            System.out.println("ReadCompletionHandler.failed:" + e);
        }
    }

    private void doWrite(String currentTime) {
        if (currentTime != null && currentTime.trim().length() > 0) {

            byte[] bytes = currentTime.getBytes();

            final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);

            writeBuffer.put(bytes);

            writeBuffer.flip();

            channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    // 如果没有发送完成，继续发送
                    if (buffer.hasRemaining()) {
                        channel.write(buffer, buffer, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        System.out.println("doWrite channel close exception:" + e);
                    }
                }
            });

        }
    }
}
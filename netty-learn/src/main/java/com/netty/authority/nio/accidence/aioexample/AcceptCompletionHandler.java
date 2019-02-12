package com.netty.authority.nio.accidence.aioexample;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {

        attachment.asynchronousServerSocketChannel.accept(attachment, this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        result.read(byteBuffer, byteBuffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();

        attachment.latch.countDown();
    }
}
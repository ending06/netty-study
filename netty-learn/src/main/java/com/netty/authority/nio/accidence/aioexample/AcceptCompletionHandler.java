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

        // 异步接收新的客户端请求
        attachment.asynchronousServerSocketChannel.accept(attachment, this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //1param:接收缓冲区，用于从异步的channel中读取数据包；2param:异步channel携带的附件，通知回调的时候作为入参使用；3:接收通知回调的handler
        result.read(byteBuffer, byteBuffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();

        attachment.latch.countDown();
    }
}
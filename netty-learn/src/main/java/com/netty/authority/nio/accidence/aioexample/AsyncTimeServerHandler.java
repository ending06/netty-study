package com.netty.authority.nio.accidence.aioexample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;

        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();

            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));

            System.out.println("the time server is start,port=" + port);
        } catch (IOException e) {
            System.out.println("init AsyncTimeServerHandler exception:" + e);
        }

    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);

        doAccept();

        try {
            // 允许当前线程一直阻塞，除非latch=0|interrupt
            latch.await();
        } catch (InterruptedException e) {

            System.out.println("latch wait exception:" + e);
        }
    }

    /**
     * 接收客户端连接，异步
     * */
    private void doAccept() {
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
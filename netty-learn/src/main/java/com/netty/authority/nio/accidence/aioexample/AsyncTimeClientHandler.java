package com.netty.authority.nio.accidence.aioexample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private AsynchronousSocketChannel asynchronousScClient;

    private String host;

    private int port;

    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            asynchronousScClient = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            System.out.println("init asynSocketClient Channel exception:" + e);
        }
    }

    @Override
    public void run() {

        latch = new CountDownLatch(1);

        asynchronousScClient.connect(new InetSocketAddress(host, port), this, this);

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("run method latch await exception:" + e);
        }

        try {
            asynchronousScClient.close();
        } catch (IOException e) {
            System.out.println("client socket close exception:" + e);
        }
    }

    @Override
    public void completed(Void result, final AsyncTimeClientHandler attachment) {

        final byte[] req = "QUERYTIMEORDER".getBytes();

        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);

        writeBuffer.put(req);

        writeBuffer.flip();

        asynchronousScClient.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    asynchronousScClient.write(buffer, buffer, this);
                } else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                    asynchronousScClient.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();

                            byte[] bytes = new byte[attachment.remaining()];

                            attachment.get(bytes);

                            String body = "init";

                            try {
                                body = new String(bytes, "UTF-8");

                                System.out.println("Now is :" + body);

                                latch.countDown();

                            } catch (UnsupportedEncodingException e) {
                                System.out.println("UnsupportedEncodingException.exception:" + e);
                            }

                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                asynchronousScClient.close();
                            } catch (IOException e) {
                                System.out.println("buffer.hasRemaining ia false  branch,some exception occur:" + e);
                            }
                            latch.countDown();
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                try {
                    asynchronousScClient.close();
                } catch (IOException e) {
                    System.out.println("buffer.hasRemaining ia true  branch,some exception occur:" + e);
                }

                latch.countDown();
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        exc.printStackTrace();

        try {
            asynchronousScClient.close();
        } catch (IOException e) {
            System.out.println("AsyncTimeClientHandler.failed:" + e);
        }

        latch.countDown();
    }
}
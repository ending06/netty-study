package com.netty.authority.nio.accidence.nioexample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/11<p>
// -------------------------------------------------------

public class MultipleTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverChannel;

    private volatile boolean stop;

    public MultipleTimeServer(int port) {
        try {

            serverChannel = ServerSocketChannel.open();

            serverChannel.configureBlocking(false);

            serverChannel.socket().bind(new InetSocketAddress(port), 1024);

            selector = Selector.open();

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println(" the time server is start,port=" + port);

        } catch (IOException e) {
            System.out.println("contruct multipleTimeServer exception:" + e);
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                SelectionKey key = null;

                while (iterator.hasNext()) {
                    key = iterator.next();

                    iterator.remove();

                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        System.out.println("hanldeInput.exception:"+e);
                        if (key != null) {
                            key.cancel();

                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("stop is false, run exception:" + e);
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                System.out.println("selector close exception:" + e);
            }
        }
    }

    private void handleInput(SelectionKey key) throws Exception {
        if (key.isValid()) {
            // 处理新接入的请求
            if (key.isAcceptable()) {
                // accept the new connection
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();

                // receive client request and build the socket 完成tcp的三次握手
                SocketChannel sc = ssc.accept();

                // 将socket设置为非阻塞模式
                sc.configureBlocking(false);

                // add new connection to the selector
                sc.register(selector, SelectionKey.OP_ACCEPT);
            }

            if (key.isReadable()) {
                // read the data
                SocketChannel sc = (SocketChannel) key.channel();

                ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                int readBytes = sc.read(readBuffer);

                if (readBytes > 0) {
                    // 调换这个buffer的当前位置，并且设置当前位置是0。说的意思就是：将缓存字节数组的指针设置为数组的开始序列即数组下标0。这样就可以从buffer开头，对该buffer进行遍历（读取）了
                    readBuffer.flip();

                    byte[] bytes = new byte[readBuffer.remaining()];

                    readBuffer.get(bytes);

                    String body = new String(bytes, "UTF-8");

                    System.out.println("the time server receive order:" + body);

                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(
                            System.currentTimeMillis()).toString() : "BAD ORDER";

                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    // 对端链接关闭
                    key.cancel();
                    sc.close();
                } else {
                    System.out.println("读到0字节，忽略");
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();

            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);

            writeBuffer.put(bytes);

            writeBuffer.flip();

            sc.write(writeBuffer);
        }
    }
}
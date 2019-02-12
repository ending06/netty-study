package com.netty.authority.nio.accidence.nioexample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class TimeClientHandle implements Runnable {

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    /***
     * 初始化NIO多路复用器和SocketChannel对象
     * */
    public TimeClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;

        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();
            // 需要将socket设置为异步非阻塞方式
            socketChannel.configureBlocking(false);

        } catch (IOException e) {
            System.out.println("init construct exception:" + e);
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            // 发送连接请求
            doConnect();
        } catch (IOException e) {
            System.out.println("the time client connect exception:" + e);
            System.exit(1);
        }

        // 轮询多路复用器selector 当有就绪的selector时，则执行handleinput方法
        while (!stop) {
            try {
                selector.select(1000);

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> itkeys = keys.iterator();

                SelectionKey key = null;

                while (itkeys.hasNext()) {
                    key = itkeys.next();

                    itkeys.remove();

                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        System.out.println("handleinput exception:" + e);

                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("selectot logic code handle exception:" + e);
                System.exit(1);
            }
        }

        // 多路复用器关闭后，所有注册到上边的channel,pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                System.out.println("selector close exception:" + e);
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        // 若当前key处于连接状态，则表示服务器已经返回ACK应答消息，之后便可对连接结果做判断
        if (key.isValid()) {
            // 判断是否连接成功
            SocketChannel socketChannel = (SocketChannel) key.channel();

            if (key.isConnectable()) {
                // finishConnect方法返回true,则说明客户端已经连接成功，返回false|Exception 则为连接失败
                if (socketChannel.finishConnect()) {
                    // 监听网络读操作
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 发送请求消息都服务器
                    doWrite(socketChannel);
                }
            } else {
                System.out.println("connect failed.exit");
                System.exit(1);
            }

            if (key.isReadable()) {
                // 由于事先无法判断应答码流的大小，就预先设置1M的空间
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                // socketchannel的read方法是异步读取操作，所以必须对结果进行判断
                int readBytes = socketChannel.read(readBuffer);

                if (readBytes > 0) {
                    readBuffer.flip();

                    byte[] bytes = new byte[readBuffer.remaining()];

                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");

                    System.out.println("Now is:" + body);

                    this.stop = true;
                } else if (readBytes < 0) {
                    System.out.println("peer link colsed");

                    key.cancel();
                    socketChannel.close();
                } else {
                    System.out.println("read 0 bytes,ignore");
                }
            }
        }

    }

    private void doConnect() throws IOException {
        // 如果连接成功，则注册到多路复用器上，发送请求消息，读应答
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            // 未连接成功，则说明服务器没有返回TCP的握手应答消息，但这并不代表连接失败，当服务器返回syn-ack后，selector就可执行轮询操作
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();

        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);

        writeBuffer.put(req);

        writeBuffer.flip();

        socketChannel.write(writeBuffer);

        // 由于发送请求是异步的，故会存在"半包写"的问题，可以通过hasRemaining判断
        if (!writeBuffer.hasRemaining()) {
            System.out.println("send order 2 succeed!");
        }
    }
}
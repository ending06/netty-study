package com.netty.server.demo.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/10/22</p>
// -------------------------------------------------------

public class PlainNioServer {

    public void serve(int port) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        serverSocket.bind(inetSocketAddress); //绑定服务器到制定端口

        Selector selector = Selector.open();//打开 selector 处理 channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册 ServerSocket 到 ServerSocket ，并指定这是专门意接受 连接

        final ByteBuffer msg = ByteBuffer.wrap("HI".getBytes());

        for (;;) {
            selector.select();//等待新的事件来处理。这将阻塞，直到一个事件是传入

            Set<SelectionKey> readyKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                iterator.remove();

                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();

                        SocketChannel client = server.accept();

                        client.configureBlocking(false);

                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());

                        System.out.println("Accepted connection from " + client);
                    }

                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();

                        ByteBuffer buffer = (ByteBuffer) key.attachment();

                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }
                } catch (IOException e) {
                    key.cancel();

                    try {
                        key.channel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }

    }
}
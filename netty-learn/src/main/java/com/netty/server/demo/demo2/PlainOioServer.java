package com.netty.server.demo.demo2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 18/10/22</p>
// JDK 阻塞io:连接好客户端，并且向客户端发送字符串“Hi!”的信息，信息发送完之后连接就断开
// -------------------------------------------------------

public class PlainOioServer {

    public void serve(int port) throws IOException {

        final ServerSocket serverSocket = new ServerSocket(port);

        for(;;){
            final Socket clientSocket = serverSocket.accept();
            System.out.println("accepted connection from:===>"+clientSocket);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    OutputStream outputStream = null;
                    try {
                        outputStream = clientSocket.getOutputStream();

                        outputStream.write("Hi".getBytes(Charset.forName("UTF-8")));
                    } catch (IOException e) {
                        e.printStackTrace();

                        try {
                            clientSocket.close();
                        } catch (IOException e1) {
                            System.out.println("close.exception===>");
                            e1.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
package com.netty.authority.nio.accidence.threadpoolsmode;

import com.netty.authority.nio.accidence.peertopeermode.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/11<p>
// 伪异步NIO---线程池模式
// -------------------------------------------------------

public class TimerServerByThreadPool {

    public static void main(String[] args) {
        int port = 8080;

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);

            System.out.println(" the time server is start in port:" + port);

            Socket socket = null;

            TimeServerHandlerExecutePool singleExcutor = new TimeServerHandlerExecutePool(50, 10000);

            while (true) {
                socket = serverSocket.accept();

                singleExcutor.execute(new TimeServerHandler(socket));
            }

        } catch (IOException e) {
            System.out.println("IOException.exception:" + e);
        } finally {
            if (serverSocket != null) {
                System.out.println("the time server is close");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("the time server close exception:" + e);
                }
                serverSocket = null;
            }
        }
    }
}
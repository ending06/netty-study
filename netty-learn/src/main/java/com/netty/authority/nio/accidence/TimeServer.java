package com.netty.authority.nio.accidence;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/11<p>
// -------------------------------------------------------

public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);

            System.out.println("the time server is start in port:" + port);

            Socket socket = null;

            while (true) {
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println("IOException.exception:" + e);
        } finally {
            if (serverSocket != null) {
                System.out.println("the time server close");
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
package com.netty.authority.nio.accidence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/11<p>
// -------------------------------------------------------

public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;

        Socket socket = null;

        BufferedReader in = null;

        PrintWriter out = null;

        try {
            socket = new Socket("127.0.0.1", port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("QUERY TIME ORDER");

            System.out.println("Send Order 2 Server succeed.");

            String response = in.readLine();

            System.out.println("Now is:" + response);

        } catch (IOException e) {
            System.out.println("IOException.exception:" + e);
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("BufferedReader close exception:" + e);
                }
                in = null;
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("socket close exception:" + e);
                }
                socket = null;
            }
        }
    }
}
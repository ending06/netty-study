package com.netty.authority.nio.accidence.peertopeermode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/11<p>
// -------------------------------------------------------

public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader in = null;

        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            out = new PrintWriter(this.socket.getOutputStream(), true);

            String currentTime = null;

            String body = null;

            while (true) {
                body = in.readLine();

                if (body == null) {
                    break;
                }

                System.out.println("the time server receive order:" + body);

                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis())
                        .toString() : "BAD ORDER";

                System.out.printf("currentTime===>"+currentTime);

                out.print(currentTime);
            }
        } catch (IOException e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    System.out.println("try to close BufferedReader exception:" + e1);
                }
            }

            if (out != null) {
                out.close();
                out = null;
            }

            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    System.out.println("try to close socket exception:" + e1);
                }
                this.socket = null;
            }

        }

    }
}
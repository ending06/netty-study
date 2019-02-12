package com.netty.authority.nio.accidence.nioexample;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/11<p>
// -------------------------------------------------------

public class NioTimeServer {

    public static void main(String[] args) {
        int port = 8080;

        MultipleTimeServer timeServer = new MultipleTimeServer(port);

        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }

}
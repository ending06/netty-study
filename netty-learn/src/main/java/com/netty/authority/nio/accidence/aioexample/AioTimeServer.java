package com.netty.authority.nio.accidence.aioexample;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
// -------------------------------------------------------

public class AioTimeServer {

    public static void main(String[] args) {
        int port = 8080;

        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);

        new Thread(timeServer, "AIO-AsynTimeServerHandler-001").start();
    }
}
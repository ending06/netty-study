package com.netty.authority.nio.accidence.aioexample;

//--------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
//-------------------------------------------------------

public class AioTimeClient {

    public static void main(String[] args) {
        int port = 8080;
        new Thread(new AsyncTimeClientHandler("127.0.0.1",port),"AIO-timeClientHandler-001").start();
    }
}
package com.netty.authority.nio.accidence.nioexample;

//--------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/12<p>
//-------------------------------------------------------

public class NioTimeClient {

    public static void main(String[] args) {
        int port = 8080;

        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClinet-001").start();
    }
}
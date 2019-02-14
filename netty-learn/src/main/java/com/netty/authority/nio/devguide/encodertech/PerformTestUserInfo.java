package com.netty.authority.nio.devguide.encodertech;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class PerformTestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("Welcome to Netty");

        int loop = 1000000;

        ByteArrayOutputStream bos = null;

        ObjectOutputStream os = null;

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);
            os.flush();
            os.close();

            byte[] b = bos.toByteArray();
            bos.close();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("the jDK seriablizable cst time is:" + (endTime - startTime) + "ms");
        System.out.println("--------------");

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();

        for (int i = 0; i < loop; i++) {
            byte[] b = userInfo.codeC(byteBuffer);
        }

        endTime = System.currentTimeMillis();
        System.out.println("the byte array serializable cost time is :" + (endTime - startTime) + "ms");
    }
}
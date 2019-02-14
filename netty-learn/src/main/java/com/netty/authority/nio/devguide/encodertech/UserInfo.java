package com.netty.authority.nio.devguide.encodertech;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/14<p>
// -------------------------------------------------------

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private int userId;

    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo buildUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] codeC() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byte[] value = this.userName.getBytes();

        byteBuffer.putInt(value.length);
        byteBuffer.put(value);
        byteBuffer.putInt(this.userId);
        byteBuffer.flip();

        value = null;

        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);

        return result;
    }

    public byte[] codeC(ByteBuffer buffer) {
        buffer.clear();
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userId);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);

        return result;
    }

    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("Welcome to Netty");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ObjectOutputStream os = new ObjectOutputStream(byteArrayOutputStream);

        os.writeObject(userInfo);

        os.flush();

        os.close();

        byte[] b = byteArrayOutputStream.toByteArray();

        System.out.println("The JDK serializable length is :" + b.length);

        byteArrayOutputStream.close();

        System.out.println("=============");

        System.out.println("The byte array seriablizable length is :" + userInfo.codeC().length);

    }
}
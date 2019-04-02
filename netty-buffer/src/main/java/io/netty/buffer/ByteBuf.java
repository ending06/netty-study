package io.netty.buffer;

import io.netty.util.ReferenceCounted;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/4/2<p>
// -------------------------------------------------------

public abstract class ByteBuf implements ReferenceCounted, Comparable<ByteBuf> {

    /**
     * 返回buffer的容量
     * */
    public abstract int capacity();

    /**
     * 调整缓存容量大小
     * current buffer size > newCapacity size:当前buffer的内容会被截断
     * current buffer size < newCapacity size:会追加一个buffer，其内容未知，大小为(newCapacity size-current buffer size)大小的buffer，
     * */
    public abstract ByteBuf capacity(int newCapacity);
}
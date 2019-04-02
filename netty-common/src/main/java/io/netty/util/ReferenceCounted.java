package io.netty.util;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/4/2<p>
// -------------------------------------------------------

public interface ReferenceCounted {

    /**
     * 返回计数个数
     * */
    int refCnt();

    /**
     * 计数加1
     * */
    ReferenceCounted retain();

    /**
     * 引用计数加increment
     * */
    ReferenceCounted retain(int increment);

    /**
     * debug调试，记录访问当前对象的位置
     * */
    ReferenceCounted touch();

    ReferenceCounted touch(Object hint);

    /**
     * 减小计数，每次减1
     * */
    boolean release();

    /**
     * 减小计数，每次减decrement
     * */
    boolean release(int decrement);
}
package io.netty.util.internal.logging;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/4/3<p>
// 内部使用的logger
// -------------------------------------------------------

public interface InternalLogger {

    /**
     * return the name of this instance
     * */
    String name();

    /**
     * 开启trace日志
     * */
    boolean isTraceEnabled();

    void trace(String name);

    void trace(String format, Object msg);

    void trace(String format, Object... arguments);

    void trace(Throwable t);

    boolean isDebugEnabled();

    void debug(String msg);

    void debug(String format, Object arg);

    void debug(String format, Object argA, Object argsB);

}
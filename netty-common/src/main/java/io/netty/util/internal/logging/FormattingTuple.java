package io.netty.util.internal.logging;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/4/10<p>
// 格式化数组
// -------------------------------------------------------

public final class FormattingTuple {
    private final String message;

    private final Throwable throwable;

    public FormattingTuple(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
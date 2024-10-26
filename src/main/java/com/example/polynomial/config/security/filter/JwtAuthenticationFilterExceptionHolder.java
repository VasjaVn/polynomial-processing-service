package com.example.polynomial.config.security.filter;

public class JwtAuthenticationFilterExceptionHolder {

    private static final ThreadLocal<Throwable> exceptionThreadLocal = new ThreadLocal<>();

    public static void set(Throwable throwable) {
        exceptionThreadLocal.set(throwable);
    }

    public static Throwable get() {
        return exceptionThreadLocal.get();
    }

    public static Throwable getAndClear() {
        Throwable throwable = get();
        clear();
        return throwable;
    }

    public static void clear() {
        exceptionThreadLocal.remove();
    }
}

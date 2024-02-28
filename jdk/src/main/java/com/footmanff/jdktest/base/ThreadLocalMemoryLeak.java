package com.footmanff.jdktest.base;

import org.junit.Test;

public class ThreadLocalMemoryLeak {

    @Test
    public void t1() {
        ThreadLocal<String> tl = new ThreadLocal<>();
        tl.set("init");

        tl.get();

        tl.remove();
    }

    @Test
    public void t2() throws Exception {
        ThreadLocal local = new ThreadLocal();
        local.set(new ThreadLocalMemoryLeak());
        local = null;
        // 手动触发GC，此时ThreadLocal被回收，那么value是否被回收呢？
        System.gc();
        // GC是异步执行的，主线程Sleep一会，等待对象回收
        Thread.sleep(1000);

        // 结果：控制台无输出，value没有被回收，发生泄漏。
    }

    // 对象被回收时触发
    @Override
    protected void finalize() throws Throwable {
        System.err.println("对象被回收...");
    }

}

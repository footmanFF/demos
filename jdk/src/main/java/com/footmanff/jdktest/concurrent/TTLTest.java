package com.footmanff.jdktest.concurrent;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.Test;

import java.util.concurrent.*;

public class TTLTest {

    public static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    @Test
    public void t1() throws Exception {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2,
                1L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // 额外的处理，生成修饰了的对象executorService
        executorService = TtlExecutors.getTtlExecutorService(executorService);

        // 在父线程中设置
        context.set("value-set-in-parent");

        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                while (true) {
                    Thread.sleep(1000L);
                }
            });
        }
        executorService.submit(() -> {
            System.out.println("runnable: " + context.get());
//            context.remove();
//            context.set("value-updated");
//            System.out.println("runnable: updated");
        });

        String value = context.get();

        System.out.println(value);

        while (true) {
            Thread.sleep(10000L);
        }
    }

}

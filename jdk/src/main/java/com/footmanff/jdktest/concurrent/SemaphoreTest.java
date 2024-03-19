package com.footmanff.jdktest.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        //创建许可证数量为5的Semaphore
        Semaphore semaphore = new Semaphore(5);

        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            try {
                //获取一个许可证
                semaphore.acquire();
                System.out.println(threadName + "执行任务...");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放一个许可证
                semaphore.release();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(runnable);
        }

        executorService.shutdown();
    }

}

/* 开始输出：
 * pool-1-thread-1执行任务...
 * pool-1-thread-5执行任务...
 * pool-1-thread-6执行任务...
 * pool-1-thread-7执行任务...
 * pool-1-thread-3执行任务...
 * 三秒后输出：
 * pool-1-thread-4执行任务...
 * pool-1-thread-8执行任务...
 * pool-1-thread-2执行任务...
 * pool-1-thread-10执行任务...
 * pool-1-thread-9执行任务...
 */

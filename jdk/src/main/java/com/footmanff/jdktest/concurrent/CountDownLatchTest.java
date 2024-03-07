package com.footmanff.jdktest.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

    @Test
    public void t1() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println("end");
    }

}

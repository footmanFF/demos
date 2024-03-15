package com.footmanff.jdktest.concurrent;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    @Test
    public void t1() {
        ReentrantLock lock = new ReentrantLock(false);
        lock.lock();
        lock.lock();
        lock.unlock();
        lock.unlock();

        System.out.println(lock.isLocked());
    }

    @Test
    public void t2() throws Exception {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        
        new Thread(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lock.lock();
            System.out.println("sub 加锁");
            condition.signal();
            System.out.println("sub 唤醒");
            lock.unlock();
        }).start();
        
        lock.lock();
        System.out.println("main 加锁");
        condition.await();
        System.out.println("main 唤醒");
        lock.unlock();

        System.out.println("end");
    }

}

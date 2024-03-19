package com.footmanff.jdktest.concurrent;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    @Test
    public void readReadMode() throws InterruptedException {
        ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock r = rw.readLock();
        ReentrantReadWriteLock.WriteLock w = rw.writeLock();

        Thread thread0 = new Thread(() -> {
            r.lock();
            try {
                Thread.sleep(1000);
                System.out.println("Thread 1 running " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                r.unlock();
            }
        }, "t1");

        Thread thread1 = new Thread(() -> {
            r.lock();
            try {
                Thread.sleep(1000);
                System.out.println("Thread 2 running " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                r.unlock();
            }
        }, "t2");

        thread0.start();
        thread1.start();

        thread0.join();
        thread1.join();
    }

    @Test
    public void readWriteMode() throws InterruptedException {
        ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock r = rw.readLock();
        ReentrantReadWriteLock.WriteLock w = rw.writeLock();

        Thread thread0 = new Thread(() -> {
            r.lock();
            try {
                Thread.sleep(1000);
                System.out.println("Thread 1 running " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                r.unlock();
            }
        }, "t1");

        Thread thread1 = new Thread(() -> {
            w.lock();
            try {
                Thread.sleep(1000);
                System.out.println("Thread 2 running " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                w.unlock();
            }
        }, "t2");

        thread0.start();
        thread1.start();

        thread0.join();
        thread1.join();
    }

}

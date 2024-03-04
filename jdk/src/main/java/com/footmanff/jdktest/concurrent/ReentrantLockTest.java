package com.footmanff.jdktest.concurrent;

import org.junit.Test;

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
    
}

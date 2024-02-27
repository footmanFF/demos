package com.footmanff.jdktest.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyAqsLockImpl extends AbstractQueuedSynchronizer {
    
    public void t() {
        tryAcquire(1);
    }
    
}

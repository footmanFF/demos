package com.footmanff.jdktest.concurrent;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicReferenceArrayTest {
    
    @Test
    public void t1() {
        AtomicReferenceArray array = new AtomicReferenceArray(8);

        System.out.println(array);
        
        array.compareAndSet(0, null, "123");

        System.out.println(array);
    }
    
}

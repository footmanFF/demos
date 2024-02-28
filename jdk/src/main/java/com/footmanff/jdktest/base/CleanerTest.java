package com.footmanff.jdktest.base;

import org.junit.Test;
import sun.misc.Cleaner;

public class CleanerTest {

    @Test
    public void t1() throws Exception {
        Object obj = new Object();
        Cleaner cleaner = Cleaner.create(obj, () -> {
            System.out.println("clean called");
        });
        obj = null;
        System.gc();
        Thread.sleep(500L);
        System.out.println("end");
    }

}

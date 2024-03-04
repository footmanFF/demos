package com.footmanff.jdktest.collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class HashMapTest {

    @Test
    public void t2() {
        System.out.println("123".hashCode());
        System.out.println(Integer.valueOf(1).hashCode());
        System.out.println(new Object().hashCode());
        System.out.println(new ReentrantLock().hashCode());
        System.out.println(new Thread().hashCode());
    }


    @Test
    public void t3() {
        System.out.println(10000000 >>> 16);
    }

    @Test
    public void t4() {
        int n = 8;
        for (int i = 0; i < 100; i++) {
            System.out.println(i + " = " + (i & (n - 1)));
        }
    }

    @Test
    public void t5() {
        System.out.println(970498593 % 8);
        System.out.println(1 & 7);

        System.out.println(Integer.toBinaryString(7));
    }

    @Test
    public void t1() {
        HashMap<String, String> map = new HashMap<>(4, 0.75f);
        for (int i = 0; i < 122; i++) {
            String key = "k" + i;
            map.put(key, "v");
        }
        map.remove("k1");

        map.forEach((k, v) -> {
            //
        });

        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.getKey();
            entry.getValue();
        }
    }


}

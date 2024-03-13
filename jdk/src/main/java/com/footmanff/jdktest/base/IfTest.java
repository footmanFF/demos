package com.footmanff.jdktest.base;

import org.junit.Test;

public class IfTest {

    @Test
    public void t1() {
        if (true && false || true) {
            System.out.println(123);
        }
    }

}

package com.footmanff.jdktest;

import org.junit.Test;

public class AA {

    @Test
    public void t1() {
        int a = 2;
        int s = ~a;

        int r = a & s;

        System.out.println(Integer.toBinaryString(a));

        System.out.println(Integer.toBinaryString(s));

        System.out.println(Integer.toBinaryString(r) + " = " + r);
    }

    @Test
    public void t2() {
        System.out.println(Integer.toBinaryString(-1));
    }
    
}

package com.footmanff.spring.aop;

import org.springframework.stereotype.Component;

/**
 * @author footmanff on 2017/8/23.
 */
@Component
@Api("")
public class SubInterfaceTestImpl implements SubInterfaceTest{

    @Override
    public void print() {
        System.out.println("hello boy!");
    }

    private void ttt(){
        System.out.println("private");
    }

    @Override
    public void print2() {
        System.out.println("hello boy 2!");
        // ttt();
    }

    @Override
    public String getString() {
        return "hi boy";
    }

    public static void main(String[] args) {
        String[] a = ",,,,,".split(",");
    }
}

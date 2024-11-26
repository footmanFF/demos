package com.footmanff.jdktest.base;

import com.sun.org.apache.bcel.internal.classfile.InnerClass;

public class InnerClassTest {

    private int field;
    
    private InnerClass innerClass;

    public InnerClassTest() {
        field = 1;
        innerClass = new InnerClass();
        System.out.println("InnerClassTest() call");
    }

    protected void doWrite() {
        //
    }

    /**
     * 如果没有定义成static，只能在InnerClassTest内部new
     */
    protected class InnerClass {
        protected void flush() {
            doWrite();
        }
    }

    public static void main(String[] args) {
        //
    }

}
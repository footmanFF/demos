package com.footmanff.jdktest.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBuffTest {
    
    @Test
    public void t1() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        for (byte bt : "你好啊".getBytes()) {
            byteBuffer.put(bt);
        }
        byteBuffer.flip();

        byte[] b = new byte[byteBuffer.limit()];
        byteBuffer.get(b);

        System.out.println(new String(b));
    }
    
    
}

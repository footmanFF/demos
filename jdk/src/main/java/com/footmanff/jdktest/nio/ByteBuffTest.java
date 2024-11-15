package com.footmanff.jdktest.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBuffTest {
    
    @Test
    public void t1() {
        // 初始为写模式
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        for (byte bt : "你好啊".getBytes()) {
            byteBuffer.put(bt);
        }
        // 转读模式
        byteBuffer.flip();

        // 数组长度必须和可读字节数一致
        byte[] b = new byte[byteBuffer.limit()];
        byteBuffer.get(b);

        System.out.println(new String(b));
    }
    
    
}

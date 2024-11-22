package com.footmanff.jdktest.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class IOUtil {

    public static void read(SocketChannel socketChannel, ByteBuffer byteBuffer, int max) throws Exception {
        int read = socketChannel.read(byteBuffer);
        if (read == -1) {
            throw new ChannelClosedException("连接断开?");
        }
        while (read != max) {
            int r = socketChannel.read(byteBuffer);
            if (r == -1) {
                throw new ChannelClosedException("连接断开?");
            }
            read += r;
        }
    }
    
}

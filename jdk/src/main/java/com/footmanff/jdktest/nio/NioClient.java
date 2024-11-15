package com.footmanff.jdktest.nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClient {

    public static void main(String[] args) throws Exception {
        SocketAddress sa = new InetSocketAddress("127.0.0.1", 8081);
        SocketChannel channel = SocketChannel.open(sa);
        channel.configureBlocking(false);

        while (channel.finishConnect()) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            if (input.equals("exit")) {
                channel.close();
                return;
            }
            log("input: " + input);

            int maxLength;
            byte[] data;
            if ("100M".equals(input)) {
                maxLength = SelectSockets.MAX_LENGTH;
                data = new byte[maxLength];
            } else {
                data = input.getBytes();
                maxLength = data.length;
            }
            ByteBuffer buffer = ByteBuffer.allocate(maxLength);
            buffer.put(data);
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            log("write");
            buffer.clear();

            // 等待服务端传回数据了再读取
            Thread.sleep(500L);

            ByteBuffer readBuffer = buffer;
            while (true) {
                int r = channel.read(readBuffer);
                if (r <= 0) {
                    break;
                }
                printResult(readBuffer);
            }
        }
    }

    private static void log(String s) {
        System.out.println(s);
    }

    private static void printResult(ByteBuffer readBuffer) {
        readBuffer.flip();
        byte[] dst = new byte[readBuffer.limit()];
        readBuffer.get(dst);
        if (dst.length <= 1024) {
            System.out.println("echo: " + dst.length + " " + new String(dst));
        } else {
            System.out.println("echo:" + dst.length);
        }
        readBuffer.clear();
    }

}

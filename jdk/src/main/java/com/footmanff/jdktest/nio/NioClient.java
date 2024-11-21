package com.footmanff.jdktest.nio;

import cn.hutool.core.util.ObjectUtil;
import com.footmanff.jdktest.base.T;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClient {

    public void start2() throws Exception {
        Scanner scanner = new Scanner(System.in);
        log("模式 1-单线程命令 2-多线程，循环开启-读写数据-关闭连接 3-粘包、分包问题测试");
        String mode = scanner.next();

        if ("1".equals(mode)) {
            new NioClient().start();
        } else if ("2".equals(mode)) {
            log("输入线程数");
            String thread = scanner.next();
            Integer t;
            try {
                t = Integer.valueOf(thread);
            } catch (Throwable e) {
                log("线程数定义错误");
                return;
            }
            new NioClient().startMultiThread(t);
            scanner.next();
        } else if ("3".equals(mode)) {
            new NioClient().start3();
        } else {
            log("模式不合法");
        }
    }

    public void startMultiThread(Integer thread) throws Exception {
        SocketAddress sa = new InetSocketAddress("127.0.0.1", 8081);
        for (int i = 0; i < thread; i++) {
            final int idx = i;
            new Thread(() -> {
                try {
                    while (true) {
                        SocketChannel channel = SocketChannel.open(sa);
                        channel.configureBlocking(false);
    
                        byte[] data = "测试内容123".getBytes();
    
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.put(data);
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            channel.write(buffer);
                        }
                        log("thread " + idx + " write");
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

                        channel.close();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    // ObjectUtil#serialize
    // ObjectUtil#deserialize
    
    
    public void start3() throws Exception {
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
            
            Message message = new Message();
            message.setContent(input);

            byte[] data = ObjectUtil.serialize(message);
            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
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

    public void start() throws Exception {
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

            byte[] data = input.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
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
        System.out.println("[Client] " + s);
    }

    private static void printResult(ByteBuffer readBuffer) {
        readBuffer.flip();
        byte[] dst = new byte[readBuffer.limit()];
        readBuffer.get(dst);
        if (dst.length <= 1024) {
            System.out.println("[Client] " + "echo: " + dst.length + " " + new String(dst));
        } else {
            System.out.println("[Client] " + "echo:" + dst.length);
        }
        readBuffer.clear();
    }

}

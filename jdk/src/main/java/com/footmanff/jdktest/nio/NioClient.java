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
import java.util.concurrent.CountDownLatch;

public class NioClient {

    public static void main(String[] args) throws Exception {
        NioClient nioClient = new NioClient();
        nioClient.start2();
    }

    public void start2() throws Exception {
        Scanner scanner = new Scanner(System.in);
        log("模式 1-单线程命令（解决粘包、分包） 2-多线程，循环开启-读写数据-关闭连接");
        String mode = scanner.next();

        if ("1".equals(mode)) {
            new NioClient().start3();
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
            new CountDownLatch(1).await();
        } else {
            log("模式不合法");
        }
    }

    public void startMultiThread(Integer thread) throws Exception {
        SocketAddress sa = new InetSocketAddress("127.0.0.1", 8081);
        for (int i = 0; i < thread; i++) {
            new Thread(() -> {
                SocketChannel channel = null;
                try {
                    while (true) {
                        channel = SocketChannel.open(sa);
                        channel.configureBlocking(false);
                        try {
                            sendAndReceive("测试内容123", channel);
                        } catch (ChannelClosedException e) {
                            log("服务端关闭了连接");
                            continue;
                        } catch (IOException e) {
                            log("IOException " + e.getMessage());
                            continue;
                        } finally {
                            channel.close();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

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

            sendAndReceive(input, channel);
        }
    }

    private void sendAndReceive(String input, SocketChannel channel) throws Exception {
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
        // log("write");
        buffer.clear();

        // 等待服务端传回数据了再读取
        Thread.sleep(500L);

        // 读第一个整形，标记消息体内容长度
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);

        IOUtil.read(channel, lengthBuffer, 4);

        int length = lengthBuffer.getInt(0);

        // 根据消息体长度，读消息体
        ByteBuffer dataBuffer = ByteBuffer.allocate(length);

        IOUtil.read(channel, dataBuffer, length);

        Response result = ObjectUtil.deserialize(dataBuffer.array(), Response.class);

        log("返回结果 " + result.toString());
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

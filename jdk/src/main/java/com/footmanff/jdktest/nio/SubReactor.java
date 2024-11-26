package com.footmanff.jdktest.nio;

import cn.hutool.core.util.ObjectUtil;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class SubReactor implements Runnable {

    private int num;
    private final Selector selector;
    private Thread thread;
    private final AtomicReference<Boolean> state = new AtomicReference<>(Boolean.FALSE);
    private final ConcurrentLinkedDeque<SocketChannel> queue;
    private final ThreadPoolExecutor pool;

    public SubReactor(int num) throws Exception {
        this.num = num;
        selector = Selector.open();
        thread = new Thread(this, "SubReactor-" + this.num);
        queue = new ConcurrentLinkedDeque<>();
        pool = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5000));
    }

    public void register(SocketChannel acceptChannel) throws Exception {
        queue.add(acceptChannel);
        log("SubReactor-" + this.num + " register OP_READ");
        selector.wakeup();
    }

    public void start() {
        if (!state.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            return;
        }
        thread.start();
    }

    @Override
    public void run() {
        try {
            run_();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run_() throws Exception {
        while (true) {
            log("SubReactor-" + num + " select() 开始执行");
            int r = selector.select();
            SocketChannel acceptChannel;
            while ((acceptChannel = queue.poll()) != null) {
                acceptChannel.register(selector, SelectionKey.OP_READ);
            }
            if (r == 0) {
                continue;
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    try {
                        Message message = readDataFromSocket(socketChannel);
                        log(message.toString());

                        pool.submit(() -> {
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            Response response = new Response();
                            response.setCode("SUCCESS");
                            response.setMsg(message.getContent());

                            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                            key.attach(response);
                            selector.wakeup();
                        });
                    } catch (ChannelClosedException e) {
                        log("SubReactor-" + this.num + " 客户端断开连接");
                        key.cancel();
                        key.channel().close();
                    } catch (Throwable e) {
                        log("readClose", e);
                        key.cancel();
                        key.channel().close();
                    }
                } else if (key.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    Response response = (Response) key.attachment();
                    if (response != null) {
                        byte[] data = ObjectUtil.serialize(response);
                        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
                        buffer.putInt(data.length);
                        buffer.put(data);
                        buffer.flip();

                        while (buffer.hasRemaining()) {
                            int result = socketChannel.write(buffer);
                            // log("写入字节数：" + result);
                            if (result <= 0) {
                                break;
                            }
                        }
                        buffer.clear();
                    } else {
                        log("requestBuffer == null");
                    }
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                }
                it.remove();
            }
        }
    }

    /**
     * 解决粘包、分包版本
     */
    protected Message readDataFromSocket(SocketChannel socketChannel) throws Exception {
        // 读第一个整形，标记消息体内容长度
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        read(socketChannel, lengthBuffer, 4);
        int length = lengthBuffer.getInt(0);

        // 根据消息体长度，读消息体
        ByteBuffer dataBuffer = ByteBuffer.allocate(length);
        read(socketChannel, dataBuffer, length);
        return ObjectUtil.deserialize(dataBuffer.array(), Message.class);
    }

    private void read(SocketChannel socketChannel, ByteBuffer byteBuffer, int max) throws Exception {
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

    private void log(String s) {
        System.out.println("[Server] " + s);
    }

    private void log(String s, Throwable e) {
        System.out.print("[Server] " + s);
        System.out.print("[Server] " + "    ");
        e.printStackTrace(System.out);
        System.out.println();
    }

}

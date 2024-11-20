package com.footmanff.jdktest.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

public class SubReactor implements Runnable {

    private int num;
    private final Selector selector;
    private Thread thread;
    private final AtomicReference<Boolean> state = new AtomicReference<>(Boolean.FALSE);
    private final ConcurrentLinkedDeque<SocketChannel> queue;

    public SubReactor(int num) throws Exception {
        this.num = num;
        selector = Selector.open();
        thread = new Thread(this, "SubReactor-" + this.num);
        queue = new ConcurrentLinkedDeque<>();
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
                    ByteBuffer requestBuffer = null;
                    try {
                        requestBuffer = readDataFromSocket(socketChannel);
                        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                        key.attach(requestBuffer);
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
                    ByteBuffer requestBuffer = (ByteBuffer) key.attachment();
                    if (requestBuffer != null) {
                        while (requestBuffer.hasRemaining()) {
                            int result = socketChannel.write(requestBuffer);
                            log("写入字节数：" + result);
                            if (result <= 0) {
                                break;
                            }
                        }
                        requestBuffer.clear();
                    } else {
                        log("requestBuffer == null");
                    }
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                }
                it.remove();
            }
        }
    }

    protected ByteBuffer readDataFromSocket(SocketChannel socketChannel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = socketChannel.read(buffer);
        if (num == -1) {
            throw new ChannelClosedException("连接断开?");
        }
        if (num > 0) {
            log("读取字节数：" + num);
        }
        buffer.flip();
        return buffer;
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

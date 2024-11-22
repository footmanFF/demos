package com.footmanff.jdktest.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author footmanff on 2017/11/23.
 */
public class NioServer {

    public static final int MAX_LENGTH = 1024;

    public static void main(String[] argv) throws Exception {
        new NioServer().go();
    }

    public void go() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        Selector selector = Selector.open();
        serverSocket.bind(new InetSocketAddress(8081));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            log("select() 开始执行");
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel acceptChannel = server.accept();
                    acceptChannel.configureBlocking(false);
                    acceptChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer requestBuffer = null;
                    try {
                        requestBuffer = readDataFromSocket(socketChannel);
                    } catch (Throwable e) {
                        log("readClose", e);
                        key.cancel();
                        key.channel().close();
                    }
                    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    key.attach(requestBuffer);
                } else if (key.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer requestBuffer = (ByteBuffer) key.attachment();
                    if (requestBuffer != null) {
                        while (requestBuffer.hasRemaining()) {
                            int result = socketChannel.write(requestBuffer);
                            // log("写入字节数：" + result);
                            if (result <= 0) {
                                break;
                            }
                        }
                        requestBuffer.clear();
                    } else {
                        log("requestBuffer == null");
                    }
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                } else {
                    log("another");
                }
                it.remove();
            }
        }
    }

    protected ByteBuffer readDataFromSocket(SocketChannel socketChannel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_LENGTH);
        int num = socketChannel.read(buffer);
        if (num == -1) {
            throw new RuntimeException("连接断开?");
        }
        if (num > 0) {
            log("读取字节数：" + num);
        }
        buffer.flip();
        return buffer;
    }

    private void log(String s) {
        System.out.println(s);
    }

    private void log(String s, Throwable e) {
        System.out.print(s);
        System.out.print("    ");
        e.printStackTrace(System.out);
        System.out.println();
    }

}

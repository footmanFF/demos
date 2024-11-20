package com.footmanff.jdktest.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MainReactor implements Runnable {

    private final List<SubReactor> subReactorList;

    private final AtomicInteger seq;

    private Thread thread;

    private final AtomicReference<Boolean> state = new AtomicReference<>(Boolean.FALSE);

    public MainReactor() throws Exception {
        seq = new AtomicInteger(0);
        subReactorList = new ArrayList<>();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            SubReactor subReactor = new SubReactor(i);
            subReactor.start();
            subReactorList.add(subReactor);
        }
    }

    @Override
    public void run() {
        try {
            run_();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (!state.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            return;
        }
        thread = new Thread(this, "MainReactor");
        thread.start();
    }

    public void run_() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        Selector selector = Selector.open();
        serverSocket.bind(new InetSocketAddress(8081));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            log("MainReactor select() 开始执行");
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    log("MainReactor accept");
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel acceptChannel = server.accept();
                    acceptChannel.configureBlocking(false);
                    SubReactor subReactor = subReactorList.get(seq.getAndIncrement() % subReactorList.size());
                    subReactor.register(acceptChannel);
                }
                it.remove();
            }
        }
    }

    private void log(String s) {
        System.out.println("[Server] "+ s);
    }

}

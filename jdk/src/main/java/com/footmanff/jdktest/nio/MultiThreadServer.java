package com.footmanff.jdktest.nio;

import com.footmanff.jdktest.base.T;

public class MultiThreadServer {

    public static void main(String[] args) throws Exception {
        MainReactor mainReactor = new MainReactor();
        mainReactor.start();

        Thread.sleep(1000L);
        
        NioClient nioClient = new NioClient();
        nioClient.start2();
    }

}

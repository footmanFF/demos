package com.fm.java.jmx;

/**
 * @author zhangli on 2017/10/2.
 */
public class Server {

    private long startTime;

    public Server() {   }

    public int start(){
        startTime = System.currentTimeMillis();
        return 0;
    }

    public long getUpTime(){
        return System.currentTimeMillis() - startTime;
    }
}
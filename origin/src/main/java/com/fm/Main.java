package com.fm;

import java.util.concurrent.CompletableFuture;

public class Main {
    
    public static void main(String[] args) throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(Main::fetchPrice);
        // 如果执行成功:
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
            throw new RuntimeException("thenAccept 异常");
        });
        // 如果执行异常:
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });

        try {
            cf.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("xx");
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(20000);
    }
    
    static Double fetchPrice() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // throw new RuntimeException("fetch price failed!");
        return 1.0D;
    }
}
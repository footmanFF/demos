package com.fm;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {

    @Test
    public void t5() throws Exception {
        CompletableFuture<String> resultFuture = new CompletableFuture<>();

        System.out.println(resultFuture.get());
        
        System.out.println("end");
    }

    @Test
    public void t4() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete("123");
        }).start();
        System.out.println("start");

        System.out.println(future.get(10, TimeUnit.SECONDS));
    }

    @Test
    public void t3() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "A";
        });
        /*
         * 前一个future执行完，继续执行 thenCompose 指定的函数，并用上一个future执行的结果作为参数
         */
        future.thenCompose(param -> {
            return CompletableFuture.supplyAsync(() -> {
                return param + " > B";
            });
        }).thenAccept(result -> {
            System.out.println("accept: " + result);
        }).whenComplete((r, t) -> {
            System.out.println("accept: " + r + " " + t);
        });
    }

    @Test
    public void t2() throws Exception {
        System.out.println("start");
        CompletableFuture<String> future = new CompletableFuture<>();
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            future.complete("A");
        });
        t.start();
        t.join();
        System.out.println(future.get());
    }

    @Test
    public void t1() throws Exception {
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://finance.sina.com.cn/code/");
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://money.163.com/code/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://finance.sina.com.cn/price/");
        });
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://money.163.com/price/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }

    static String queryCode(String name, String url) {
        System.out.println("query code from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }
}
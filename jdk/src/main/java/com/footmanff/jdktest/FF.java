package com.footmanff.jdktest;

import com.alibaba.fastjson.JSON;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FF {
    public static final String BULK_UPSERT_SCRIPT = "for (int i = 0; i < params.updateList.length; i++) {\n" +
            "    if (ctx._source[params.updateList[i].name] == null \n" +
            "        ||ctx._source[params.updateList[i].name].ver < params.updateList[i].data.ver\n" +
            "        ||(ctx._source[params.updateList[i].name].utime < params.updateList[i].data.utime)\n" +
            "        ||(ctx._source[params.updateList[i].name].ver <= params.updateList[i].data.ver && ctx._source[params.updateList[i].name].utime <= params.updateList[i].data.utime)\n" +
            "    ) { \n" +
            "        ctx._source[params.updateList[i].name] = params.updateList[i].data \n" +
            "    } else {\n" +
            "        ctx.op = 'none'\n" +
            "    } \n" +
            "}";
    public static void main(String[] args) throws InterruptedException {
        System.out.println(BULK_UPSERT_SCRIPT);
    }

}

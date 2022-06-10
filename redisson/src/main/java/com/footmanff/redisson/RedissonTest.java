package com.footmanff.redisson;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.time.Duration;

/**
 * Hello world!
 */
public class RedissonTest {

    @Test
    public void t1() throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient client = Redisson.create(config);
        RAtomicLong longObject = client.getAtomicLong("myLong");
        longObject.incrementAndGet();

        Duration duration = Duration.ofSeconds(4);
        longObject.expireIfNotSet(duration);

        while (true) {
            Thread.sleep(500L);
            System.out.println(longObject.get());
        }
    }

}

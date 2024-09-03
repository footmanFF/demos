package com.footmanff.jdktest.base;

import org.junit.Test;

import java.time.Clock;

public class ClockTest {

    @Test
    public void t1() {
        Clock clock = Clock.systemUTC();
        System.out.println(clock.millis());
        System.currentTimeMillis();
    }

    public static void main(String[] args) {
        String sql = "alter table mp_pay_{db}.refund_order_detail_{tb} change `ext` `ext` text DEFAULT NULL COMMENT '扩展字段';";

        for (int i = 0; i < 8; i++) {
            String s = sql.replace("{db}", "" + i);
            s = s.replace("{tb}", "" + 0);
            System.out.println(s);
            
            s = sql.replace("{db}", "" + i);
            s = s.replace("{tb}", "" + 1);
            System.out.println(s);
        }
    }

}

package com.footmanff.jdktest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Formatter;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        BigDecimal orderItemMaxAmount = new BigDecimal("0.02");
        BigDecimal applyItemNum = new BigDecimal("5");
        BigDecimal lastItemNum = new BigDecimal("6");
        BigDecimal maxApplyAmount = orderItemMaxAmount.multiply(applyItemNum).divide(lastItemNum, 5, RoundingMode.HALF_DOWN);

        System.out.println(maxApplyAmount);
    }


}

package com.footmanff.jdktest.base;

import org.apache.commons.text.RandomStringGenerator;

import java.math.BigDecimal;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

public class F {

    private final static RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(LETTERS, DIGITS)
            .build();

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(randomStringGenerator.generate(10));
        }
    }
}

package com.footmanff.jdktest.collection;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {
    
    @Test
    public void t1() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("1", "");
        map.put("2", "");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry);
        }

        map.put("1", "");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry);
        }
    }
    
}

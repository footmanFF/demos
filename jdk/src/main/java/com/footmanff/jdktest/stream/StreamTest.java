package com.footmanff.jdktest.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamTest {

    @Test
    public void t1() {
        List<Integer> list = new ArrayList<>();
        list.add(1);

        Set<String> set = list.stream().map(Object::toString).collect(Collectors.toSet());

        list.stream().filter(e -> e == 1);
    }

}

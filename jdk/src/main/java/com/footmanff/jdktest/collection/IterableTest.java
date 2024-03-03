package com.footmanff.jdktest.collection;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Iterator;

public class IterableTest {

    @Test
    public void t1() {
        MyCollection myCollection = new MyCollection();
        for (Object o : myCollection) {
            System.out.println(o);
        }
    }

    static class MyCollection implements Iterable<String> {

        @NotNull
        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                int idx = 0;

                @Override
                public boolean hasNext() {
                    return idx <= 10;
                }

                @Override
                public String next() {
                    return "idx = " + (idx++);
                }
            };
        }
    }

}

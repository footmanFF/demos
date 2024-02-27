//package com.footmanff.mockneat;
//
//import net.andreinc.mockneat.MockNeat;
//import net.andreinc.mockneat.types.enums.IPv4Type;
//import org.junit.Test;
//
///**
// * @author footmanff on 2018/8/17.
// */
//public class FirstSample {
//
//    @Test
//    public void test() {
//        MockNeat mock = MockNeat.threadLocal();
//
//        Integer int1 = mock.ints()
//                           .range(100, 200)
//                           .map(i -> i / 5)
//                           .val();
//
//        System.out.println(int1);
//
//        String ipv4ClassA = mock.ipv4s()
//                                .types(IPv4Type.CLASS_A)
//                                .val();
//
//        System.out.println(ipv4ClassA);
//    }
//
//    @Test
//    public void test2() {
//        MockNeat mock = MockNeat.threadLocal();
//
//        mock.reflect(User.class);
//
//        System.out.println(mock.strings().val());
//        System.out.println(mock.days().val());
//        System.out.println(mock.localDates().val());
//        System.out.println(mock.localDates().toUtilDate().val());
//    }
//
//}

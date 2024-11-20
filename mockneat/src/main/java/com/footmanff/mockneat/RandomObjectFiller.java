//package com.footmanff.mockneat;
//
//import net.andreinc.mockneat.MockNeat;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//
///**
// * @author footmanff on 2018/8/17.
// */
//public class RandomObjectFiller {
//
//    public static <T> T createAndFill(Class<T> clazz) throws Exception {
//        T instance = clazz.newInstance();
//        for (Field field : clazz.getDeclaredFields()) {
//            if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
//                continue;
//            }
//            field.setAccessible(true);
//            Object value = getRandomValueForField(field);
//            field.set(instance, value);
//        }
//        return instance;
//    }
//
//    private static Object getRandomValueForField(Field field) throws Exception {
//        MockNeat mock = MockNeat.threadLocal();
//        Class<?> type = field.getType();
//
//        if (type.isEnum()) {
//            Object[] enumValues = type.getEnumConstants();
//            return enumValues[mock.ints().range(0, enumValues.length).val()];
//        } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
//            return mock.ints().range(0, 1000).val();
//        } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
//            return mock.longs().range(0, 1000).val();
//        } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
//            return BigDecimal.valueOf(mock.doubles().range(0, 1000).val()).setScale(2, RoundingMode.HALF_UP).doubleValue();
//        } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
//            return BigDecimal.valueOf(mock.floats().range(0, 1000).val()).setScale(2, RoundingMode.HALF_UP).floatValue();
//        } else if (type.equals(String.class)) {
//            return mock.strings().size(10).val().toLowerCase();
//        } else if (type.equals(BigDecimal.class)) {
//            return BigDecimal.valueOf(mock.doubles().range(0, 1000).val()).setScale(2, RoundingMode.HALF_UP);
//        } else if (type.equals(Date.class)) {
//            return mock.localDates().toUtilDate().val();
//        } else if (type.equals(List.class) || type.equals(Set.class) || type.equals(Map.class)) {
//            return null;
//        }
//        return createAndFill(type);
//    }
//
//    public static void main(String[] args) throws Exception {
//        User user = createAndFill(User.class);
//    }
//
//}

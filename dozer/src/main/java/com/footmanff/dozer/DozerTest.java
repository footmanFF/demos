package com.footmanff.dozer;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.List;

public class DozerTest {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public static void main(String[] args) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(100L);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = new Order();
        order.setOrderItem(orderItem);
        order.setOrderItemList(orderItemList);

        Order orderCopy = new Order();

        dozer.map(order, orderCopy);

        Order orderCopy2 = dozer.map(order, Order.class);

        int a = 1;
    }

}

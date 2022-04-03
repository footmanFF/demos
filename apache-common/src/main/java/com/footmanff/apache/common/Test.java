package com.footmanff.apache.common;

import org.apache.commons.beanutils.BeanUtils;
public class Test {

    public static void main(String[] args) throws Exception {
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(100L);

        Order order = new Order();
        order.setOrderItem(orderItem);

        Order orderCopy = (Order) BeanUtils.cloneBean(order);
        System.out.println(1);
    }

}

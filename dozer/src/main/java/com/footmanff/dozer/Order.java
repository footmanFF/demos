package com.footmanff.dozer;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private List<OrderItem> orderItemList;

    private OrderItem orderItem;

}

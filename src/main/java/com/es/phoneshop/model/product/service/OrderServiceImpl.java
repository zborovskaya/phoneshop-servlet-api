package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.Order;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static final OrderService instance = new OrderServiceImpl();

    private OrderServiceImpl() {
    }

    public OrderService getInstance() {
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems()
                .stream()
                .collect(Collectors.toList()));
        order.getTotalCost();
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setSubTotalCost(BigDecimal.valueOf(0).add(cart.getTotalCost()));
        return order;
    }
}

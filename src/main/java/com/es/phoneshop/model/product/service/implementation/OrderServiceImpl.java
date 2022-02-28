package com.es.phoneshop.model.product.service.implementation;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.dao.ArrayListOrderDao;
import com.es.phoneshop.model.product.dao.implementation.OrderDao;
import com.es.phoneshop.model.product.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static final OrderService instance = new OrderServiceImpl();
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    private OrderServiceImpl() {
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    public static OrderService getInstance() {
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems()
                .stream().map(item -> {
                    try {
                        return item.clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).collect(Collectors.toList()));
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setSubTotalCost(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubTotalCost().add(order.getDeliveryCost()));
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }
}

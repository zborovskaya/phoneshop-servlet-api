package com.es.phoneshop.model.product.service.implementation;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.service.PaymentMethod;

import java.util.List;

public interface OrderService {
    Order getOrder(Cart cart);

    List<PaymentMethod> getPaymentMethods();

    void placeOrder(Order order);
}

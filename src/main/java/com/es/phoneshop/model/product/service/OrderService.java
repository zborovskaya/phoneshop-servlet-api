package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.Order;

public interface OrderService {
    Order getOrder(Cart cart);
}

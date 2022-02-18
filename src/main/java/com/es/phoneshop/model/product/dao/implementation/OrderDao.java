package com.es.phoneshop.model.product.dao.implementation;

import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

public interface OrderDao {
    Order getOrderBySecureId(String id) throws OrderNotFoundException;

    Order getEntity(Long id) throws ProductNotFoundException;

    void save(Order objectDao);
}

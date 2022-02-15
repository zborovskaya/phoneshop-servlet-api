package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.bean.Cart;

import javax.servlet.http.HttpSession;
import java.text.NumberFormat;

public interface CartService {
    Cart getCart(HttpSession session);

    void add(Cart cart, Long productId, String quantity, NumberFormat format) throws QuantityException;

    void update(Cart cart, Long productId, String quantity, NumberFormat format) throws QuantityException;

    void delete(Cart cart, Long productId);
}

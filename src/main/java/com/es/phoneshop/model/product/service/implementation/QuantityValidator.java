package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.bean.Product;

public class QuantityValidator {
    private final static String NUMBER_PATTERN = "[0-9]+";

    public static boolean isNumber(String quantity) {
        return quantity.matches(NUMBER_PATTERN);
    }

    public static boolean isEnoughStock(int quantity, Product product) {
        return product.getStock() >= quantity;
    }
}

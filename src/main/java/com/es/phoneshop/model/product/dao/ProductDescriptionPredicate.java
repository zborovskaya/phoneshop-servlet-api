package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.bean.Product;

import java.util.function.Predicate;

public class ProductDescriptionPredicate implements Predicate<Product> {
    private String query;

    public ProductDescriptionPredicate(String query) {
        this.query = query;
    }

    @Override
    public boolean test(Product product) {
        if (query == null) return true;
        if (query.isEmpty()) return true;
        String[] queryWords = query.split("\\s+");
        String[] productWords = product.getDescription().split("\\s+");
        for (String word : queryWords) {
            for (String query : productWords) {
                if (word.equals(query)) return true;
            }
        }
        return false;
    }
}

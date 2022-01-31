package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.bean.Product;

import java.util.Comparator;

public class ProductDescriptionComparator implements Comparator<Product> {
    private String query;

    public ProductDescriptionComparator(String query) {
        this.query = query;
    }

    @Override
    public int compare(Product product1, Product product2) {
        if (query == null || query.isEmpty()) return 0;

        int countWordsProduct1 = 0;
        int countWordsProduct2 = 0;
        String[] queryWords = query.split("\\s+");
        String[] product1Words = product1.getDescription().split("\\s+");
        String[] product2Words = product2.getDescription().split("\\s+");
        for (String word : queryWords) {
            for (String query : product1Words) {
                if (word.equals(query)) countWordsProduct1++;
            }
        }
        for (String word : queryWords) {
            for (String query : product2Words) {
                if (word.equals(query)) countWordsProduct2++;
            }
        }
        return countWordsProduct1 < countWordsProduct2 ? 1 : countWordsProduct1 == countWordsProduct2 ? 0 : -1;
    }
}

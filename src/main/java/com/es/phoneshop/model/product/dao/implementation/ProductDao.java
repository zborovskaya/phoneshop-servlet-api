package com.es.phoneshop.model.product.dao.implementation;

import com.es.phoneshop.model.product.service.implementation.SortOrder;
import com.es.phoneshop.model.product.service.implementation.SortField;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.bean.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    List<Product> findProducts(String code, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock);

    void delete(Long id) throws ProductNotFoundException;

    Product getEntity(Long id) throws ProductNotFoundException;

    void save(Product objectDao);
}

package com.es.phoneshop.model.product.dao.implementation;

import com.es.phoneshop.model.product.bean.GenericBean;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

public interface GenericDao<T extends GenericBean> {
    T getEntity(Long id) throws ProductNotFoundException;

    void save(T objectDao);
}

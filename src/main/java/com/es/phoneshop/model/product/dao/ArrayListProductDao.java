package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import com.es.phoneshop.model.product.service.implementation.SortOrder;
import com.es.phoneshop.model.product.service.implementation.SortField;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.bean.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericDaoImpl<Product> implements ProductDao {
    private static volatile ProductDao instance;
    private ProductDescriptionComparator productDescriptionComparator;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private ArrayListProductDao() {
        this.dataResource = new ArrayList<>();
    }

    public static ProductDao getInstance() {
        ProductDao result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ProductDao.class) {
            if (instance == null) {
                instance = new ArrayListProductDao();
            }
            return instance;
        }
    }


    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        productDescriptionComparator = new ProductDescriptionComparator(query);
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField != null && SortField.DESCRIPTION == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }
        List<Product> productsFinding = dataResource.stream()
                .filter(new ProductDescriptionPredicate(query))
                .filter(p -> p.getPrice() != null)
                .filter(p -> p.getStock() > 0)
                .sorted(comparator)
                .sorted(productDescriptionComparator)
                .collect(Collectors.toList());
        readLock.unlock();
        return productsFinding;
    }


    @Override
    public void delete(Long id) throws ProductNotFoundException {
        writeLock.lock();
        boolean exist = false;
        for (Product product : dataResource) {
            if (id.equals(product.getId())) {
                dataResource.remove(product);
                exist = true;
                break;
            }
        }
        if (!exist) throw new ProductNotFoundException();
        writeLock.unlock();
    }
}

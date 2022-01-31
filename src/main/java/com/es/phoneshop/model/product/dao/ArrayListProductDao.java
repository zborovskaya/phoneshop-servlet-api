package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.bean.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static volatile ProductDao instance;
    private List<Product> products;
    private long productId;
    private ProductDescriptionComparator productDescriptionComparator;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
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
    public Product getProduct(Long id) throws ProductNotFoundException {
        readLock.lock();
        Product product = products
                .stream()
                .filter(p -> id.equals(p.getId()))
                .findAny()
                .orElseThrow(() -> {
                    readLock.unlock();
                    return new ProductNotFoundException();
                });
        readLock.unlock();
        return product;
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
        List<Product> productsFinding = products.stream()
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
    public void save(Product product) {
        writeLock.lock();
        if (product.getId() == null) {
            product.setId(++productId);
            products.add(product);
            writeLock.unlock();
        } else {
            Long id = product.getId();
            for (int index = 0; index < products.size(); index++) {
                if (id.equals(products.get(index).getId())) {
                    products.set(index, product);
                    break;
                }
            }
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundException {
        writeLock.lock();
        boolean exist = false;
        for (Product product : products) {
            if (id.equals(product.getId())) {
                products.remove(product);
                exist = true;
                break;
            }
        }
        if (!exist) throw new ProductNotFoundException();
        writeLock.unlock();
    }
}

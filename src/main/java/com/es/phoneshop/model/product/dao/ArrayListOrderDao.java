package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.dao.implementation.OrderDao;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends GenericDaoImpl<Order> implements OrderDao {
    private static volatile OrderDao instance;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private ArrayListOrderDao() {
        this.dataResource = new ArrayList<>();
    }

    public static OrderDao getInstance() {
        OrderDao result = instance;
        if (result != null) {
            return result;
        }
        synchronized (OrderDao.class) {
            if (instance == null) {
                instance = new ArrayListOrderDao();
            }
            return instance;
        }
    }

    @Override
    public Order getOrderBySecureId(String id) throws OrderNotFoundException {
        readLock.lock();
        Order order = dataResource
                .stream()
                .filter(o -> id.equals(o.getSecureId()))
                .findAny()
                .orElseThrow(() -> {
                    readLock.unlock();
                    return new OrderNotFoundException();
                });
        readLock.unlock();
        return order;
    }
}

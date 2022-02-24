package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.bean.GenericBean;
import com.es.phoneshop.model.product.dao.implementation.GenericDao;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericDaoImpl<T extends GenericBean> implements GenericDao<T> {
    private long entityId;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    protected List<T> dataResource;

    @Override
    public T getEntity(Long id) throws ProductNotFoundException {
        readLock.lock();
        T objectDao = dataResource
                .stream()
                .filter(p -> id.equals(p.getId()))
                .findAny()
                .orElseThrow(() -> {
                    readLock.unlock();
                    return new ProductNotFoundException();
                });
        readLock.unlock();
        return objectDao;
    }

    @Override
    public void save(T objectDao) {
        writeLock.lock();
        if (objectDao.getId() == null) {
            objectDao.setId(++entityId);
            dataResource.add(objectDao);
            writeLock.unlock();
        } else {
            Long id = objectDao.getId();
            for (int index = 0; index < dataResource.size(); index++) {
                if (id.equals(dataResource.get(index).getId())) {
                    dataResource.set(index, objectDao);
                    break;
                }
            }
        }
    }
}

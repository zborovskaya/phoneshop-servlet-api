package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProductDaoImplTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ProductDaoImpl();
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts().isEmpty());
    }
}

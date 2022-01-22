package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException{
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertTrue(product.getId()!=null && product.getId()>0);
        Product result = productDao.getProduct(Long.valueOf(product.getId()));
        assertEquals(product,result);
    }

    @Test
    public void testFindWithZeroStock() throws ProductNotFoundException{
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        List<Product> result = productDao.findProducts();
        assertFalse(result.contains(product));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException{
        assertNotNull(productDao.getProduct(2L).getId());
        productDao.delete(2L);
        productDao.getProduct(2L);
    }
}

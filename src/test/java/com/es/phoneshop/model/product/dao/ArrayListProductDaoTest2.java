package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.bean.PriceHistory;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.web.DemoDataContextListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest2 {
    @Mock
    private ServletContextEvent servletContextEvent;
    @Mock
    private ServletContext servletContext;
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private Currency usd = Currency.getInstance("USD");
    private DemoDataContextListener demoDataContextListener = new DemoDataContextListener();
    ;
    private Product result;
    private Product product1;
    private Product product3;

    @Before
    public void setup() throws ServletException {
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        demoDataContextListener.contextInitialized(servletContextEvent);
        product1 = new Product("sgs", "Samsung Galaxy S", new PriceHistory(LocalDate.now(), new BigDecimal(100)), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product1.setId(1L);
        product3 = new Product("sgs3", "Samsung Galaxy S III", new PriceHistory(LocalDate.now(), new BigDecimal(300)), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        product3.setId(3L);
    }

    @Test
    public void getProduct() {
        result = productDao.getProduct(1L);
        assertEquals(result, product1);
    }

    @Test
    public void findProducts() {
        List<Product> result = productDao.findProducts("Samsung", null, null);
        List<Product> actual = new ArrayList<>();
        actual.add(product1);
        actual.add(product3);
        assertEquals(result, actual);
    }


    @Test(expected = ProductNotFoundException.class)
    public void deleteNotFoundProduct() throws ProductNotFoundException {
        productDao.delete(100L);
        System.out.println(3);
    }

    @Test(expected = ProductNotFoundException.class)
    public void getNotFoundProduct() throws ProductNotFoundException {
        productDao.getProduct(100L);
        System.out.println(4);
    }
}
package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.bean.PriceHistory;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ProductDaoSavingTest {
    @Spy
    private List<Product> spyProducts = new ArrayList<>();
    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Test
    public void save() {
        WhiteboxImpl.setInternalState(productDao, "dataResource", spyProducts);
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new PriceHistory(LocalDate.now(),
                new BigDecimal(100)), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                        "Samsung/Samsung%20Galaxy%20S.jpg");
        doReturn(true).when(spyProducts).add(product);
        productDao.save(product);

        Mockito.verify(spyProducts).add(product);
    }
}

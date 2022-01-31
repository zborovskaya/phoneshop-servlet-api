package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.PriceHistory;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceHistoryPageServletTest extends TestCase {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ProductDao mockProductDao;

    private PriceHistoryPageServlet servlet = new PriceHistoryPageServlet();

    @Before
    public void setup() throws ServletException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new PriceHistory(LocalDate.now(),
                new BigDecimal(100)), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                        "Samsung/Samsung%20Galaxy%20S.jpg");
        servlet.init(servletConfig);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getParameter(anyString())).thenReturn("1");
        WhiteboxImpl.setInternalState(servlet, "productDao", mockProductDao);
        when(mockProductDao.getProduct( anyLong())).thenReturn(product);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getParameter(anyString());
        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }
}
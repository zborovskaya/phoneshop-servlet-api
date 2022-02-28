package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.PriceHistory;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.OrderService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
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
    @Mock
    private CartService mockCartService;
    @Mock
    private OrderService mockOrderService;
    @Mock
    private HttpSession httpSession;

    private static final String PRODUCTS = "products";
    private static final String ORDER = "order";
    private static final String PAYMENT_METHODS = "paymentMethods";
    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private Cart cart;

    @Before
    public void setup() throws ServletException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new PriceHistory(LocalDate.now(),
                new BigDecimal(100)), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                        "Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(1L);
        cart = new Cart();
        servlet.init(servletConfig);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(httpSession);
        WhiteboxImpl.setInternalState(servlet, "productDao", mockProductDao);
        WhiteboxImpl.setInternalState(servlet, "cartService", mockCartService);
        WhiteboxImpl.setInternalState(servlet, "orderService", mockOrderService);
        when(mockProductDao.getEntity(anyLong())).thenReturn(product);
        cart.getItems().add(new CartItem(product.getId(), 1));
        when(mockCartService.getCart(httpSession)).thenReturn(cart);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ORDER), any());
        verify(request).setAttribute(eq(PRODUCTS), any());
        verify(request).setAttribute(eq(PAYMENT_METHODS), any());
        verify(requestDispatcher).forward(request, response);
    }
}
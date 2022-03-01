package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.PriceHistory;
import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.dao.implementation.OrderDao;
import com.es.phoneshop.model.product.service.implementation.OrderServiceImpl;
import com.es.phoneshop.model.product.service.implementation.PaymentMethod;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest extends TestCase {
    @Mock
    private OrderDao orderDaoMock;
    private final int QUANTITY = 1;
    private Cart cart;
    private OrderService orderService = OrderServiceImpl.getInstance();
    private Product product;

    @Before
    public void setup() {
        cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        product = new Product("sgs", "Samsung Galaxy S", new PriceHistory(LocalDate.now(),
                new BigDecimal(100)), usd, 100,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                        "Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(1L);
        cart.getItems().add(new CartItem(product.getId(), QUANTITY));
        cart.setTotalQuantity(QUANTITY);
        cart.setTotalCost(new BigDecimal(100));
    }

    @Test
    public void givenCart_WhenGetOrder_ThenReturnNewOrder() {
        Order order = new Order();
        List<CartItem> cartItemList = new ArrayList<>();
        order.setItems(cartItemList);
        order.getItems().add(new CartItem(product.getId(), 1));
        order.setDeliveryCost(new BigDecimal(5));
        order.setSubTotalCost(new BigDecimal(100));
        order.setTotalCost(new BigDecimal(105));
        order.setTotalQuantity(1);

        Order result = orderService.getOrder(cart);

        assertEquals(order, result);
    }

    @Test
    public void saveOrder_WhenPlaceOrder() {
        WhiteboxImpl.setInternalState(orderService, "orderDao", orderDaoMock);
        Order order = new Order();
        doNothing().when(orderDaoMock).save(any());

        orderService.placeOrder(order);

        verify(orderDaoMock).save(any());
    }

    @Test
    public void returnPaymentMethods_WhenGetPaymentMethods() {
        List<PaymentMethod> result = new ArrayList<>();
        result.add(PaymentMethod.CACHE);
        result.add(PaymentMethod.CREDIT_CARD);

        List<PaymentMethod> actual = orderService.getPaymentMethods();

        assertEquals(actual, result);
    }
}
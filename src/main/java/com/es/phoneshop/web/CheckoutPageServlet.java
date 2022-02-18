package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import com.es.phoneshop.model.product.service.OrderServiceImpl;
import com.es.phoneshop.model.product.service.PaymentMethod;
import com.es.phoneshop.model.product.service.CartServiceImpl;
import com.es.phoneshop.model.product.service.implementation.CartService;
import com.es.phoneshop.model.product.service.implementation.OrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private static final String PRODUCTS = "products";
    private static final String ORDER = "order";
    private static final String ERRORS = "errors";
    private static final String PAYMENT_METHODS = "paymentMethods";
    private static final String VALUE_IS_REQUIRED = "Value is required";
    private ProductDao productDao;
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        request.setAttribute(ORDER, order);
        setAttribute(request, order);
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);

        setRequiredValue(request, "firstName", errors, order::setFirstName);
        setRequiredValue(request, "lastName", errors, order::setLastName);
        setRequiredValue(request, "phone", errors, order::setPhone);

        setRequiredValue(request, "deliveryAddress", errors, order::setDeliveryAddress);
        setPaymentMethod(request, errors, order::setPaymentMethod);
        setDeliveryDate(request, errors, order::setDeliveryDate);
        request.setAttribute(ERRORS, errors);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute(ORDER, order);
            setAttribute(request, order);
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    private void setRequiredValue(HttpServletRequest request, String parameter, Map<String, String> errors,
                                  Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, VALUE_IS_REQUIRED);
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors,
                                  Consumer<PaymentMethod> consumer) {
        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            errors.put("paymentMethod", VALUE_IS_REQUIRED);
        } else {
            consumer.accept(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors,
                                 Consumer<LocalDate> consumer) {

        String deliveryDate = request.getParameter("deliveryDate");
        if (deliveryDate == null || deliveryDate.isEmpty()) {
            errors.put("deliveryDate", VALUE_IS_REQUIRED);
        } else {
            consumer.accept(LocalDate.parse(deliveryDate));
        }
    }

    private void setAttribute(HttpServletRequest request, Order order) {
        List<Product> productsOrder = new ArrayList<>();
        for (CartItem item : order.getItems()) {
            productsOrder.add(productDao.getEntity(item.getProductId()));
        }
        request.setAttribute(PRODUCTS, productsOrder);
        request.setAttribute(PAYMENT_METHODS, orderService.getPaymentMethods());
    }
}

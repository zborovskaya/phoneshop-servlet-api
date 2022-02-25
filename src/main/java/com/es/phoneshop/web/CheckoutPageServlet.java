package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import com.es.phoneshop.model.product.service.ClientInfoValidator;
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
import javax.servlet.http.HttpSession;
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
    private static final String NAME_VALUES = "nameValues";
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
        setAttribute(request);
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Map<String, String> errors = new HashMap<>();
        Map<String, String> nameValues = new HashMap<>();
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);

        setNameValue(request, "firstName", errors, nameValues, order::setFirstName);
        setNameValue(request, "lastName", errors, nameValues, order::setLastName);
        setPhoneValue(request, "phone", errors, nameValues, order::setPhone);
        setRequiredValue(request, "deliveryAddress", errors, nameValues, order::setDeliveryAddress);
        setPaymentMethod(request, errors, nameValues, order::setPaymentMethod);
        setDeliveryDate(request, errors, nameValues, order::setDeliveryDate);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            httpSession.setAttribute(ORDER, order);
            httpSession.setAttribute(ERRORS, errors);
            httpSession.setAttribute(NAME_VALUES, nameValues);
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }

    private void setRequiredValue(HttpServletRequest request, String parameter, Map<String, String> errors,
                                  Map<String, String> nameValues, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, VALUE_IS_REQUIRED);
            nameValues.put(parameter, value);
        } else {
            consumer.accept(value);
        }
    }

    private void setPhoneValue(HttpServletRequest request, String parameter, Map<String, String> errors,
                               Map<String, String> nameValues, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (!ClientInfoValidator.isPhoneValid(value)) {
            errors.put(parameter, VALUE_IS_REQUIRED);
            nameValues.put(parameter, value);
        } else {
            consumer.accept(value);
        }
    }

    private void setNameValue(HttpServletRequest request, String parameter, Map<String, String> errors,
                              Map<String, String> nameValues, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (!ClientInfoValidator.isNameValid(value)) {
            errors.put(parameter, VALUE_IS_REQUIRED);
            nameValues.put(parameter, value);
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors,
                                  Map<String, String> nameValues, Consumer<PaymentMethod> consumer) {
        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            errors.put("paymentMethod", VALUE_IS_REQUIRED);
            nameValues.put("paymentMethod", paymentMethod);
        } else {
            consumer.accept(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors,
                                 Map<String, String> nameValues, Consumer<LocalDate> consumer) {

        String deliveryDate = request.getParameter("deliveryDate");
        if (deliveryDate == null || deliveryDate.isEmpty()) {
            errors.put("deliveryDate", VALUE_IS_REQUIRED);
            nameValues.put("deliveryDate", deliveryDate);
        } else {
            consumer.accept(LocalDate.parse(deliveryDate));
        }
    }

    private void setAttribute(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Map<Long, String> errors = (Map<Long, String>) httpSession.getAttribute(ERRORS);
        Map<Long, String> nameValues = (Map<Long, String>) httpSession.getAttribute(NAME_VALUES);
        Cart cart = cartService.getCart(request.getSession());
        Order order = null;
        if (errors != null) {
            request.setAttribute(ERRORS, errors);
            httpSession.removeAttribute(ERRORS);
            request.setAttribute(NAME_VALUES, nameValues);
            httpSession.removeAttribute(NAME_VALUES);
            order = (Order) httpSession.getAttribute(ORDER);
            httpSession.removeAttribute(ORDER);
        } else {
            order = orderService.getOrder(cart);
        }
        request.setAttribute(ORDER, order);
        List<Product> productsOrder = new ArrayList<>();
        for (CartItem item : order.getItems()) {
            productsOrder.add(productDao.getEntity(item.getProductId()));
        }
        request.setAttribute(PRODUCTS, productsOrder);
        request.setAttribute(PAYMENT_METHODS, orderService.getPaymentMethods());
    }
}

package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.implementation.CartServiceImpl;
import com.es.phoneshop.model.product.exception.QuantityException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String PRODUCT_UPDATE = "?message=Cart update successfully";
    private static final String QUANTITY = "quantity";
    private static final String PRODUCTS = "products";
    private static final String PRODUCT_ID = "productId";
    private static final String CART = "cart";
    private static final String ERRORS = "errors";
    private static final String QUANTITY_ERROR = "quantityError";
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAttribute(request);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long, String> errors = new HashMap<>();
        Map<Long, String> errorsQuantity = new HashMap<>();
        String[] quantitiesString = request.getParameterValues(QUANTITY);
        String[] productsId = request.getParameterValues(PRODUCT_ID);
        HttpSession httpSession = request.getSession();
        Cart cart = cartService.getCart(httpSession);
        for (int i = 0; i < productsId.length; i++) {
            Long productId = Long.valueOf(productsId[i]);
            try {
                cartService.update(cart, productId, quantitiesString[i],
                        NumberFormat.getInstance(request.getLocale()));
            } catch (QuantityException ex) {
                errors.put(productId, ex.getMessage());
                errorsQuantity.put(productId, quantitiesString[i]);
                httpSession.setAttribute(ERRORS, errors);
                httpSession.setAttribute(QUANTITY_ERROR, errorsQuantity);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart" + PRODUCT_UPDATE);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void setAttribute(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Map<Long, String> errors = (Map<Long, String>) httpSession.getAttribute(ERRORS);
        Map<Long, String> quantityString = (Map<Long, String>) httpSession.getAttribute(QUANTITY_ERROR);
        if (errors != null) {
            request.setAttribute(ERRORS, errors);
            httpSession.removeAttribute(ERRORS);
        }
        if (quantityString != null) {
            request.setAttribute(QUANTITY_ERROR, quantityString);
            httpSession.removeAttribute(QUANTITY_ERROR);
        }
        Cart cart = cartService.getCart(request.getSession());
        List<Product> productsCart = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            productsCart.add(productDao.getEntity(item.getProductId()));
        }
        request.setAttribute(CART, cart);
        request.setAttribute(PRODUCTS, productsCart);
    }
}

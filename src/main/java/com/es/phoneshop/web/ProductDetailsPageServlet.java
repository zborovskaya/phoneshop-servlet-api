package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.bean.RecentViewCart;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;
import com.es.phoneshop.model.product.service.implementation.CartService;
import com.es.phoneshop.model.product.service.CartServiceImpl;
import com.es.phoneshop.model.product.service.implementation.RecentViewedService;
import com.es.phoneshop.model.product.service.RecentViewedServiceImpl;
import com.es.phoneshop.model.product.exception.QuantityException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.stream.Collectors;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_ADDED = "?message=Product added to cart";
    private static final String QUANTITY = "quantity";
    private static final String PRODUCT = "product";
    private static final String CART = "cart";
    private static final String RECENT_VIEWED = "recentViewed";
    private static final String ERROR = "error";
    private static final String EQUALS = "=";
    private static final String AMPERSAND = "&";
    private static final String QUESTION_MARK = "?";
    private ProductDao productDao;
    private CartService cartService;
    private RecentViewedService recentViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
        recentViewed = RecentViewedServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAttribute(request);
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter(QUANTITY);
        Long productId = parseProductId(request);
        try {
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, productId, quantityString, NumberFormat.getInstance(request.getLocale()));
            response.sendRedirect(request.getContextPath() + "/products/" + productId + PRODUCT_ADDED);
        } catch (QuantityException ex) {
            request.setAttribute(ERROR, ex.getMessage());
            response.sendRedirect(request.getContextPath() + "/products/" + productId
                    + QUESTION_MARK + QUANTITY + EQUALS + quantityString
                    + AMPERSAND + ERROR + EQUALS + ex.getMessage());
        }
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }

    private void setAttribute(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Long productId = parseProductId(request);
        request.setAttribute(PRODUCT, productDao.getEntity(productId));
        Product product = productDao.getEntity(productId);
        request.setAttribute(PRODUCT, product);
        request.setAttribute(CART, cartService.getCart(httpSession));
        RecentViewCart recentViewCart = recentViewed.getRecentViewed(httpSession);
        recentViewed.add(productId, recentViewCart);
        request.setAttribute(RECENT_VIEWED, recentViewed
                .getRecentViewed(httpSession)
                .getItems()
                .stream()
                .filter(p -> p != product)
                .collect(Collectors.toList()));
    }
}
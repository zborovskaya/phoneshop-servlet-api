package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.CartServiceImpl;
import com.es.phoneshop.model.product.service.QuantityException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String[] quantitiesString = request.getParameterValues(QUANTITY);
        String[] productsId = request.getParameterValues(PRODUCT_ID);
        Cart cart = cartService.getCart(request.getSession());
        for (int i = 0; i < productsId.length; i++) {
            Long productId = Long.valueOf(productsId[i]);
            try {
                cartService.update(cart, productId, quantitiesString[i],
                        NumberFormat.getInstance(request.getLocale()));
            } catch (QuantityException ex) {
                errors.put(productId, ex.getMessage());
                request.setAttribute(ERRORS, errors);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart" + PRODUCT_UPDATE);
        } else {
            doGet(request, response);
        }
    }

    private void setAttribute(HttpServletRequest request) {
        Cart cart = cartService.getCart(request.getSession());
        List<Product> productsCart = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            productsCart.add(productDao.getProduct(item.getProductId()));
        }
        request.setAttribute(CART, cart);
        request.setAttribute(PRODUCTS, productsCart);
    }
}

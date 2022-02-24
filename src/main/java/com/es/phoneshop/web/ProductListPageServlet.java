package com.es.phoneshop.web;

import com.es.phoneshop.model.product.service.SortField;
import com.es.phoneshop.model.product.service.SortOrder;
import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.bean.Product;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCT_ADDED = "?message=Product added to cart";
    private static final String RECENT_VIEWED = "recentViewed";
    private static final String QUERY = "query";
    private static final String PRODUCTS = "products";
    private static final String SORT = "sort";
    private static final String ORDER = "order";
    private static final String IDS = "ids";
    private static final String QUANTITY = "quantity";
    private static final String PRODUCT_ID = "productId";
    private static final String CART = "cart";
    private static final String ERROR = "error";
    private ProductDao productDao;
    private CartService cartService;
    private RecentViewedService recentViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentViewed = RecentViewedServiceImpl.getInstance();
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        String query = request.getParameter(QUERY);
        String sortField = request.getParameter(SORT);
        String sortOrder = request.getParameter(ORDER);
        SortField field = Optional.ofNullable(sortField)
                .map(s -> SortField.valueOf(s.toUpperCase())).orElse(null);
        SortOrder order = Optional.ofNullable(sortOrder)
                .map(s -> SortOrder.valueOf(sortOrder.toUpperCase()))
                .orElse(null);
        request.setAttribute(PRODUCTS, productDao.findProducts(query, field, order));
        List<Product> productList = recentViewed
                .getRecentViewed(httpSession)
                .getItems();
        if (productList.size() < 4) {
            request.setAttribute(RECENT_VIEWED, productList);
        } else {
            request.setAttribute(RECENT_VIEWED, productList
                    .stream()
                    .skip(1)
                    .collect(Collectors.toList()));
        }
        Cart cart = cartService.getCart(httpSession);
        request.setAttribute(CART, cart);
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] quantitiesString = request.getParameterValues(QUANTITY);
        String[] ids = request.getParameterValues(IDS);
        String productIdString = request.getParameter(PRODUCT_ID);
        int index = 0;
        for (int i = 0; i < ids.length; i++) {
            if (productIdString.equals(ids[i])) {
                index = i;
                break;
            }
        }
        Long productId = Long.valueOf(productIdString);
        request.getLocale();
        Cart cart = cartService.getCart(request.getSession());
        try {
            cartService.add(cart, productId, quantitiesString[index], NumberFormat.getInstance(request.getLocale()));
            response.sendRedirect(request.getContextPath() + "/products" + PRODUCT_ADDED);
        } catch (QuantityException ex) {
            request.setAttribute(ERROR, ex.getMessage());
            doGet(request, response);
        }
    }
}
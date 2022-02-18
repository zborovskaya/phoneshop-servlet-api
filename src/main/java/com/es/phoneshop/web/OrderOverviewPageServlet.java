package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.CartItem;
import com.es.phoneshop.model.product.bean.Order;
import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ArrayListOrderDao;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.implementation.OrderDao;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String PRODUCTS = "products";
    private static final String ORDER = "order";
    private ProductDao productDao;
    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        Order order = orderDao.getOrderBySecureId(secureOrderId);
        request.setAttribute(ORDER, order);
        setAttribute(request, order);
        request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
    }

    private void setAttribute(HttpServletRequest request, Order order) {
        List<Product> productsOrder = new ArrayList<>();
        for (CartItem item : order.getItems()) {
            productsOrder.add(productDao.getEntity(item.getProductId()));
        }
        request.setAttribute(PRODUCTS, productsOrder);
    }
}

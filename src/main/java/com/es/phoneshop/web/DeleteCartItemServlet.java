package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Cart;
import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.implementation.CartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private static final String PRODUCT_DELETE_SUCCESSFULLY = "?message=Product delete successfully";
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        cartService.delete(cart, parseProductId(request));
        response.sendRedirect(request.getContextPath() + "/cart" + PRODUCT_DELETE_SUCCESSFULLY);
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}

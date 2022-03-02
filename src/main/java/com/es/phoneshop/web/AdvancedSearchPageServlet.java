package com.es.phoneshop.web;

import com.es.phoneshop.model.product.bean.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.implementation.ProductDao;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productCodeString = request.getParameter("productCode");
        String minPriceString = request.getParameter("minPrice");
        String maxPriceString = request.getParameter("maxPrice");
        String minStockString = request.getParameter("minStock");

        Map<String, String> errors = new HashMap<>();

        BigDecimal minPrice = parseDecimal(minPriceString, errors, "minPriceError");
        BigDecimal maxPrice = parseDecimal(maxPriceString, errors, "maxPriceError");
        Integer minStock = parseInteger(minStockString, errors, "minStockError");
        List<Product> products = new ArrayList<>();

        if (errors.isEmpty()) {
            products = productDao.findProducts(productCodeString, minPrice, maxPrice, minStock);
            if(productCodeString==null && minPrice==null && maxPrice==null && minStock==null)
            {
                products=new ArrayList<>();
            }
        } else {
            request.setAttribute("errors", errors);
        }
        request.setAttribute("products",products);

        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private BigDecimal parseDecimal(String value, Map<String, String> errors, String errorName) {
        if (value != null && !value.isEmpty()) {
            try {
                return BigDecimal.valueOf(Double.parseDouble(value));
            } catch (NumberFormatException ex) {
                errors.put(errorName, "Not a number");
            }
        }
        return null;
    }
    private Integer parseInteger(String value, Map<String, String> errors, String errorName) {
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                errors.put(errorName, "Not a number");
            }
        }
        return null;
    }

}

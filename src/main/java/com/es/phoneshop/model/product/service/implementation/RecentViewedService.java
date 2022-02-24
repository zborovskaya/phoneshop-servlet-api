package com.es.phoneshop.model.product.service.implementation;

import com.es.phoneshop.model.product.bean.RecentViewCart;

import javax.servlet.http.HttpSession;

public interface RecentViewedService {
    RecentViewCart getRecentViewed(HttpSession session);

    void add(Long productId, RecentViewCart recentProducts);
}

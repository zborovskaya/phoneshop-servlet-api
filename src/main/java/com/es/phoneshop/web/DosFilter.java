package com.es.phoneshop.web;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.DosProtectionServiceImpl;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {
    private DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionService = DosProtectionServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(429);
        }
    }

    @Override
    public void destroy() {
    }
}

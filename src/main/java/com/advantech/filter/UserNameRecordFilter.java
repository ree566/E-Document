/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.filter;

import com.advantech.model.Identit;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.MDC;

/**
 *
 * @author Wei.Cheng
 */
public class UserNameRecordFilter implements Filter {

    protected void beforeRequest(final HttpServletRequest request, final String message) {
        final HttpSession session = request.getSession();
        final Identit user = session != null ? (Identit) session.getAttribute("user") : null;
        if (user != null) {
            MDC.put("username", user.getUsername());
        }
    }

    protected void afterRequest(final HttpServletRequest request, final String message) {
        MDC.remove("username");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}

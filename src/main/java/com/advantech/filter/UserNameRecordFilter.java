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
import org.springframework.web.filter.AbstractRequestLoggingFilter;

/**
 *
 * @author Wei.Cheng
 */
public class UserNameRecordFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(final HttpServletRequest request, final String message) {
        final HttpSession session = request.getSession();
        final Identit user = session != null ? (Identit) session.getAttribute("user") : null;
        if (user != null) {
            System.out.println("user is not null");
            MDC.put("username", user.getName());
        }else{
            System.out.println("user is null");
        }
    }

    @Override
    protected void afterRequest(final HttpServletRequest request, final String message) {
        MDC.remove("username");
    }

}

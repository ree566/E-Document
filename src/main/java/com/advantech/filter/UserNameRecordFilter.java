/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.filter;

import com.advantech.model.User;
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
        final User user = session != null ? (User) session.getAttribute("user") : null;
        if (user != null) {
            MDC.put("username", user.getUsername());
        }
    }

    @Override
    protected void afterRequest(final HttpServletRequest request, final String message) {
        MDC.remove("username");
    }

}

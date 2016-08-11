/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.filter;

import com.advantech.helper.StringParser;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Wei.Cheng
 */
public class PermissionFilter implements Filter {

    private int SYTEM_MANAGER_PERMISSION;

    @Override
    public void init(FilterConfig filterConfig) {
        String contextParam = filterConfig.getServletContext().getInitParameter("SYTEM_MANAGER_PERMISSION");
        SYTEM_MANAGER_PERMISSION = StringParser.strToInt(contextParam);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        //String loginURI = request.getContextPath() + "/";

        int userPermission = (int) session.getAttribute("permission");

        if (userPermission >= SYTEM_MANAGER_PERMISSION) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("../error/ErrorPermission");
        }

    }

    @Override
    public void destroy() {
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.filter;

import com.advantech.model.Identit;
import com.advantech.model.Permission;
import static com.advantech.model.Permission.SYSOP_PERMISSION;
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

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        Identit i = (Identit) session.getAttribute("user");
        int userPermission = i.getPermission();

        if (userPermission >= SYSOP_PERMISSION) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute("errorMessage", "Error permission");
            request.getRequestDispatcher("/pages/Error").forward(req, res);
        }

    }

    @Override
    public void destroy() {
    }

}

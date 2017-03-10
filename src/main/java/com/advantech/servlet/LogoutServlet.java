/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(urlPatterns = {"/LogoutServlet"},
        initParams = {
            @WebInitParam(name = "LOGOUT_VIEW", value = "login.jsp")}
)
public class LogoutServlet extends HttpServlet {

    private String LOGOUT_VIEW;

    @Override
    public void init() throws ServletException {
        LOGOUT_VIEW = getServletConfig().getInitParameter("LOGOUT_VIEW");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req
     * @param res
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        res.sendRedirect(LOGOUT_VIEW);
    }
}

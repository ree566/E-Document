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
@WebServlet(urlPatterns = {"/LoginServlet"},
        initParams = {
            @WebInitParam(name = "SUCCESS", value = "pages/index.jsp")
            ,
            @WebInitParam(name = "FAIL", value = "login.jsp")}
)
public class LoginServlet extends HttpServlet {

    private String SUCCESS;
    private String FAIL;

    @Override
    public void init() throws ServletException {
        SUCCESS = getServletConfig().getInitParameter("SUCCESS");
        FAIL = getServletConfig().getInitParameter("FAIL");
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

        HttpSession session = req.getSession();

        String jobnumber = req.getParameter("jobnumber");

        session.setAttribute("jobnumber", jobnumber);
        res.sendRedirect(SUCCESS);
//        req.setAttribute("errormsg", "錯誤的帳號或密碼");
//        req.getRequestDispatcher(FAIL).forward(req, res);
    }
}

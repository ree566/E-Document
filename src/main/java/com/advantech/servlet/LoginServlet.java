/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.entity.Identit;
import com.advantech.service.IdentitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
            @WebInitParam(name = "FAIL", value = "login.jsp")
        }
)
public class LoginServlet extends HttpServlet {

    private String SUCCESS;
    private String FAIL;

    @Override
    public void init() throws ServletException {
        SUCCESS = getServletConfig().getInitParameter("SUCCESS");
        FAIL = getServletConfig().getInitParameter("FAIL");
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        HttpSession session = req.getSession(false);
//        if (session != null && session.getAttribute("jobnumber") != null) {
//            res.sendRedirect(SUCCESS);
//        } else {
//            req.getRequestDispatcher(FAIL).forward(req, res);
//        }
//    }
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
        String password = req.getParameter("password");
        IdentitService service = new IdentitService();

        Identit i = service.findByJobnumber(jobnumber);
        if (i == null) {
            req.setAttribute("errormsg", "查無此人");
            req.getRequestDispatcher(FAIL).forward(req, res);
        } else if (!i.getPassword().equals(password)) {
            req.setAttribute("errormsg", "錯誤的帳號或密碼");
            req.getRequestDispatcher(FAIL).forward(req, res);
        } else {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            session.setAttribute("jobnumber", i.getJobnumber());
            session.setAttribute("name", i.getName());
            session.setAttribute("permission", i.getPermission());
            session.setAttribute("floor", gson.toJson(i.getFloor()));
            session.setAttribute("userType", gson.toJson(i.getUserType()));
            res.sendRedirect(SUCCESS);
        }
    }

}

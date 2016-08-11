package com.advantech.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/Logout"},
        initParams = {
            @WebInitParam(name = "LOGOUT_VIEW", value = "Login")}
)

public class Logout extends HttpServlet {

    private String LOGOUT_VIEW;

    @Override
    public void init() throws ServletException {
        LOGOUT_VIEW = getServletConfig().getInitParameter("LOGOUT_VIEW");
    }

    @Override
    @SuppressWarnings("null")
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(LOGOUT_VIEW);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.PageInfo;
import com.advantech.helper.ParamChecker;
import com.advantech.response.SheetViewResponse;
import com.advantech.service.SheetViewService;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "SheetViewServlet", urlPatterns = {"/SheetViewServlet"})
public class SheetViewServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req
     * @param res
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
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

        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        PageInfo info = new PageInfo();
        String rows = req.getParameter("rows");
        String page = req.getParameter("page");
        String sidx = req.getParameter("sidx");
        String sord = req.getParameter("sord");
        String searchField = req.getParameter("searchField");
        String searchString = req.getParameter("searchString");
        String searchOper = req.getParameter("searchOper");

        ParamChecker pChecker = new ParamChecker();

        if (pChecker.checkInputVals(rows)) {
            info.setRows(Integer.parseInt(rows));
        }

        if (pChecker.checkInputVals(page)) {
            info.setPage(Integer.parseInt(page));
        }

        info.setSidx(sidx);
        info.setSord(sord);
        info.setSearchField(searchField);
        info.setSearchString(searchString);
        info.setSearchOper(searchOper);

        SheetViewResponse viewResp = new SheetViewResponse();
        SheetViewService service = new SheetViewService();
        List l = (List) service.findAll(info);
        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;
        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());

        out.println(new Gson().toJson(viewResp));
    }
}

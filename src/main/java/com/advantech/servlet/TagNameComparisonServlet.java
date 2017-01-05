/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.service.BasicService;
import com.advantech.service.TagNameComparisonService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TagNameComparisonServlet", urlPatterns = {"/TagNameComparisonServlet"})
public class TagNameComparisonServlet extends HttpServlet {

    private TagNameComparisonService tcService = null;

    @Override
    public void init()
            throws ServletException {
        tcService = BasicService.getTagNameComparisonService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();


        out.print(new JSONObject().put("data", tcService.getAll()));

    }
}

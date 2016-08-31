/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.test;

import com.advantech.entity.BAB;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(TestServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String PO = req.getParameter("PO");
        String Model_name = req.getParameter("Model_name");
        String line = req.getParameter("line");
        String people = req.getParameter("people");
        if (new ParamChecker().checkInputVals(PO, Model_name, line, people)) {
            out.print(BasicService.getBabService().startBAB(new BAB(PO, Model_name, Integer.parseInt(line), Integer.parseInt(people))));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        out.println(BasicService.getBabService().getAvg(Integer.parseInt(id)));
    }
}

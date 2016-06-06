/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.servlet;

import com.advantech.helper.WebServiceRV;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.xml.transform.TransformerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "XMLServlet", urlPatterns = {"/XMLServlet"})
public class XMLServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(XMLServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        try {
            WebServiceRV rv = WebServiceRV.getInstance();
            List<String> wsData = rv.getXMLString();
            out.print(wsData.get(1));
        } catch (Exception ex) {
            log.error(ex.toString());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doGet(req, res);
    }
}

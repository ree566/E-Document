/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.servlet;

import com.advantech.webservice.WebServiceRV;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class XMLServlet {

    private static final Logger log = LoggerFactory.getLogger(XMLServlet.class);

    @RequestMapping(value = "/XMLServlet", method = {RequestMethod.GET})
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        try {
            WebServiceRV rv = WebServiceRV.getInstance();
            List<String> wsData = rv.getKanbanUsersForString();
            out.print(wsData.get(1));
        } catch (Exception ex) {
            log.error(ex.toString());
        }

    }

}

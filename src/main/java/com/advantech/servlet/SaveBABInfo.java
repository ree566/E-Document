/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 儲存資料到LS_BAB用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.entity.BAB;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "SaveBABInfo", urlPatterns = {"/SaveBABInfo"})
public class SaveBABInfo extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SaveBABInfo.class);
    private BABService babService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init() throws ServletException {
        babService = BasicService.getBabService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String po = req.getParameter("po");
        String modelName = req.getParameter("modelname");
        String line = req.getParameter("line");
        String people = req.getParameter("people");
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        JSONObject serverMsg = null;
        if (pChecker.checkInputVals(po, modelName, line, people)) {
            try {
                int lineNo = Integer.parseInt(line);
                serverMsg = new JSONObject();
                if (type.equals("1")) {
                    String str = babService.checkAndStartBAB(new BAB(po, modelName, lineNo, Integer.parseInt(people)));
                    if ("success".equals(str)) {
                        serverMsg = babService.getBABByLine(lineNo);
                        serverMsg.put("status", "success");
                    } else {
                        serverMsg = new JSONObject().put("status", str);
                    }
                } else {
                    String str = babService.closeBAB(id);
                    serverMsg = new JSONObject().put("status", !"".equals(str) ? str : "錯誤");
                }

            } catch (JSONException | NumberFormatException e) {
                log.error(e.toString());
            } catch (Exception ex) {
                log.error(ex.toString());
            } finally {
                out.print(serverMsg);
            }
        }
    }
}

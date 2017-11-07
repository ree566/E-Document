/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BabService;
import com.advantech.webservice.WebServiceRV;
import com.google.gson.Gson;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class BabSearch {

    @Autowired
    private BabService babService;
    
    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/BabSearch", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String po = req.getParameter("po");
        String saveLine = req.getParameter("saveline");
        String poGetBAB = req.getParameter("po_getBAB");
        String poSaveLine = req.getParameter("po_saveline");
        if (pChecker.checkInputVal(po)) {
            try {
                String modelname = WebServiceRV.getInstance().getModelnameByPo(po);
                out.print(modelname == null ? "data not found" : convertString(modelname));
            } catch (Exception e) {
                out.print("data not found");
            }
        } else if (pChecker.checkInputVal(saveLine)) {
            out.print(new Gson().toJson(babService.getProcessingBABByLine(Integer.parseInt(saveLine))));
        } else if (pChecker.checkInputVals(poGetBAB, poSaveLine)) {
            out.print(babService.getBABInfoWithSensorState(poGetBAB, poSaveLine));
        }
    }

    private String convertString(String input) {
        String converstring = "";
        Pattern p = Pattern.compile("[\\w|-]");
        Matcher matcher = p.matcher(input);
        while (matcher.find()) {
            converstring += matcher.group();
        }
        return converstring;
    }

}

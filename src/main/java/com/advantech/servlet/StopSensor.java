/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "StopSensor", urlPatterns = {"/StopSensor"})
public class StopSensor extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(StopSensor.class);

    private BABService babService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
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

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        JSONObject serverMsg = new JSONObject();
        try {
            String sensor = req.getParameter("sensor");
            String babId = req.getParameter("BABid");
            String line = req.getParameter("line");

            if (pChecker.checkInputVals(sensor, babId, line)) {
                JSONObject message = babService.stopSensor(Integer.parseInt(sensor), Integer.parseInt(babId));
                serverMsg.put("servermsg", message);
            } else {
                serverMsg.put("servermsg", "no data through");
            }
            out.print(serverMsg);
        } catch (JSONException ex) {
            log.error(ex.toString());
        }
    }

    public static void main(String arg0[]) {
        JSONArray arr = new JSONArray();
        arr.put("1");
        System.out.print(arr.length());
    }
}

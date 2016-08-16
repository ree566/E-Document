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
@WebServlet(name = "BABOtherStationServlet", urlPatterns = {"/BABOtherStationServlet"})
public class BABOtherStationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BABOtherStationServlet.class);

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

        String jobnumber = req.getParameter("jobnumber");
        String station = req.getParameter("station");
        String babId = req.getParameter("babId");

        String action = req.getParameter("action");

        if (pChecker.checkInputVals(action)) {
            if (pChecker.checkInputVals(station, babId, jobnumber)) {
                int babid = Integer.parseInt(babId);
                int stationid = Integer.parseInt(station);
                switch (action) {
                    case "LOGIN":
                        boolean result = babService.recordBABPeople(babid, stationid, jobnumber);
                        out.print(result ? "success" : "fail");
                        break;
                    case "SENSOR_END":
                        JSONObject message = babService.stopSensor(babid, stationid);
                        out.print(message);
                        break;
                    case "BAB_END":
                        out.print(babService.closeBAB(babid));
                        break;
                    default:
                        out.print("Not support action");
                }
            } else {
                out.print("Invaild input value");
            }
        } else {
            out.print("Not support action");
        }
    }

    public static void main(String arg0[]) {
        JSONArray arr = new JSONArray();
        arr.put("1");
        System.out.print(arr.length());
    }
}

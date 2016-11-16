/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.servlet;

import com.advantech.entity.BAB;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABLoginStatusService;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BABOtherStationServlet", urlPatterns = {"/BABOtherStationServlet"})
public class BABOtherStationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BABOtherStationServlet.class);

    private BABLoginStatusService bService = null;
    private BABService babService = null;
    private ParamChecker pChecker = null;
    private final String successMsg = "success";

    @Override
    public void init()
            throws ServletException {
        bService = BasicService.getBabLoginStatusService();
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

//        res.setContentType("application/json");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String jobnumber = req.getParameter("jobnumber");
        String station = req.getParameter("station");
        String babId = req.getParameter("babId");

        String action = req.getParameter("action");

        if (pChecker.checkInputVals(action)) {
            if (pChecker.checkInputVals(station, babId)) {
                int babid = Integer.parseInt(babId);
                int stationid = Integer.parseInt(station);

                BAB b = babService.getBAB(babid);

                if (stationid <= b.getPeople()) {
                    switch (action) {
                        case "CHANGE_USER":
                            out.print(bService.recordBABPeople(babid, stationid, jobnumber) ? "success" : "fail");
                            break;
                        case "BAB_END":
                            if (stationid == b.getPeople()) { // if the station is the last station
                                String message = babService.closeBAB(babid);
                                if (message.equals(successMsg)) {
                                    bService.recordBABPeople(babid, stationid, jobnumber);
                                }
                                out.print(message);
                            } else {
                                boolean isSensorClosed = babService.checkSensorIsClosed(babid, stationid);

                                if (isSensorClosed) {
                                    out.print("感應器已經關閉");
                                } else {
                                    JSONObject message = babService.stopSensor(babid, stationid);
                                    boolean existBabStatistics = message.getBoolean("total");
                                    boolean isPrevClose = message.getBoolean("history");
                                    boolean isStationClose = message.getBoolean("do_sensor_end");

                                    //沒有babavg，直接回傳success，等第三站關閉
                                    if (!existBabStatistics) {
                                        out.print("查無統計數據，若要關閉工單請從最後一站直接做關閉動作");
                                    } else if (!isPrevClose) {
                                        out.print("上一站尚未關閉");
                                    } else if (!isStationClose) {
                                        out.print("發生錯誤，本站尚未關閉，請聯絡管理人員");
                                    } else {
                                        bService.recordBABPeople(babid, stationid, jobnumber);
                                        out.print("success");
                                    }
                                }
                            }
                            break;

                        default:
                            out.print("Not support action");
                    }
                } else {
                    out.print("所在站別大於本工單所輸入的人數，請重新確認");
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

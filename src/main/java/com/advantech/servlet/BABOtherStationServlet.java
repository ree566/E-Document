/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.servlet;

import com.advantech.entity.BAB;
import com.advantech.entity.BABPeopleRecord;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//        res.setContentType("application/json");
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String jobnumber = req.getParameter("jobnumber");
        String station = req.getParameter("station");
        String babId = req.getParameter("babId");

        String action = req.getParameter("action");

        if (pChecker.checkInputVals(action)) {
            if (pChecker.checkInputVals(station, babId, jobnumber)) {
                int babid = Integer.parseInt(babId);
                int stationid = Integer.parseInt(station);

                if (isStationVaild(babid, stationid)) {
                    switch (action) {
                        case "LOGIN":
                            BABPeopleRecord bRecord = babService.getExistUserInBAB(babid, stationid);
                            if (bRecord.getUser_id().equals(jobnumber)) {
                                out.print("success"); //當確定人是存在的，且工號站別數已經記錄起來
                                return;
                            }
                            List<BABPeopleRecord> l = babService.getExistUserInBAB(babid);
                            boolean checkStatus = true;
                            for (BABPeopleRecord br : l) {
                                if (br.getUser_id().equals(jobnumber)) {
                                    out.print("您已經在 " + br.getBtime() + " 登入第 " + br.getStation() + " 站");
                                    checkStatus = false;
                                    break;

                                } else if (br.getStation() == stationid) {
                                    out.print("此工單本站已經有人使用(使用者: " + br.getUser_id() + " 已經在 " + br.getBtime() + " 登入)");
                                    checkStatus = false;
                                    break;
                                }
                            }

                            //檢查站別有無使用，和工號有無登入其他站別
                            if (checkStatus == true) {
                                boolean result = babService.recordBABPeople(babid, stationid, jobnumber);
                                out.print(result ? "success" : "fail");
                            }
                            break;
                        case "SENSOR_END":

                            JSONObject message = babService.stopSensor(babid, stationid);
                            boolean existBabStatistics = message.getBoolean("total");
                            boolean isPrevClose = message.getBoolean("history");
                            boolean isStationClose = message.getBoolean("do_sensor_end");

                            //沒有babavg，直接回傳success，等第三站關閉
                            if (!existBabStatistics) {
                                out.print("success");
                            } else if (!isPrevClose) {
                                out.print("上一站尚未關閉");
                            } else if (!isStationClose) {
                                out.print("發生錯誤，本站尚未關閉，請聯絡管理人員");
                            } else {
                                out.print("success");
                            }
                            break;
                        case "BAB_END":
                            out.print(babService.closeBAB(babid));

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

    private boolean isStationVaild(int BABid, int station) {
        BAB b = babService.getBAB(BABid);
        return station <= b.getPeople();
    }

    public static void main(String arg0[]) {
        JSONArray arr = new JSONArray();
        arr.put("1");
        System.out.print(arr.length());
    }
}

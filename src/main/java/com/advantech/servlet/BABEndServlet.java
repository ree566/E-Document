/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.servlet;

import com.advantech.entity.BAB;
import com.advantech.entity.BABLoginStatus;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABLoginStatusService;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "BABEndServlet", urlPatterns = {"/BABEndServlet"})
public class BABEndServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BABEndServlet.class);

    private BABLoginStatusService bService = null;
    private BABService babService = null;
    private ParamChecker pChecker = null;

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

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String station = req.getParameter("station");
        String lineNo = req.getParameter("lineNo");
        String BABid = req.getParameter("BABid");

        if (pChecker.checkInputVals(station, lineNo, BABid)) {
            int line = Integer.parseInt(lineNo);
            int stationid = Integer.parseInt(station);
            int babid = Integer.parseInt(BABid);

            BAB b = babService.getBAB(babid);//由前端先搜尋後給他得知
            
            BABLoginStatus babLoginStatus = bService.getBABLoginStatus(line, stationid);

            if (stationid <= b.getPeople()) {
                
                if (b.getPeople() == stationid) { // if the station is the last station
                    if ("success".equals(babService.closeBAB(b.getId()))) {
                        bService.recordBABPeople(b.getId(), stationid, babLoginStatus.getJobnumber());
                        out.print("success");
                    }
                } else {
                    JSONObject message = babService.stopSensor(b.getId(), stationid);
                    boolean existBabStatistics = message.getBoolean("total");
                    boolean isPrevClose = message.getBoolean("history");
                    boolean isStationClose = message.getBoolean("do_sensor_end");

                    //沒有babavg，直接回傳success，等第三站關閉
                    if (!existBabStatistics) {
                        out.print("統計資料不存在，如要關閉請由最後一站直接關閉");
                    } else if (!isPrevClose) {
                        out.print("上一站尚未關閉");
                    } else if (!isStationClose) {
                        out.print("發生錯誤，本站尚未關閉，請聯絡管理人員");
                    } else {
                        //把人員記錄起來
                        bService.recordBABPeople(b.getId(), stationid, babLoginStatus.getJobnumber());
                        out.print("success");
                    }
                }
            } else {
                out.print("所在站別大於本工單所輸入的人數，請重新確認");
            }
        } else {
            out.print("Invaild input value");
        }
    }
}

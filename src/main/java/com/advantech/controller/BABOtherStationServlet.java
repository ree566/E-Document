/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.controller;

import com.advantech.model.Bab;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.BabService;
import java.io.*;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class BABOtherStationServlet {

    private static final Logger log = LoggerFactory.getLogger(BABOtherStationServlet.class);

    @Autowired
    private BabSettingHistoryService bService;

    @Autowired
    private BabService babService;

    private final String successMsg = "success";

    @RequestMapping(value = "/BABOtherStationServlet", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam String jobnumber,
            @RequestParam int babId,
            @RequestParam int station,
            @RequestParam String action,
            HttpServletResponse res
    ) throws IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        Bab b = babService.getBAB(babId);

        if (station <= b.getPeople()) {
            switch (action) {
                case "CHANGE_USER":
                    out.print(bService.recordBABPeople(babId, station, jobnumber) ? "success" : "fail");
                    break;
                case "BAB_END":
                    if (station == b.getPeople()) { // if the station is the last station
                        String message = babService.closeBAB(babId);
                        if (message.equals(successMsg)) {
                            bService.recordBABPeople(babId, station, jobnumber);
//                            Endpoint6.syncAndEcho();
                        }
                        out.print(message);
                    } else {
                        boolean isSensorClosed = babService.checkSensorIsClosed(babId, station);

                        if (isSensorClosed) {
                            out.print("感應器已經關閉");
                        } else {
                            JSONObject message = babService.stopSensor(babId, station);
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
                                bService.recordBABPeople(babId, station, jobnumber);
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
    }
}

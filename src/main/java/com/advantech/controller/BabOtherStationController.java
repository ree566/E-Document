/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.controller;

import com.advantech.model.Bab;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.TagNameComparison;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.BabService;
import com.advantech.service.TagNameComparisonService;
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
import static com.google.common.base.Preconditions.*;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabOtherStationController")
public class BabOtherStationController {

    private static final Logger log = LoggerFactory.getLogger(BabOtherStationController.class);

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;
    
    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/changeUser", method = {RequestMethod.POST})
    @ResponseBody
    protected String changeUser(
            @RequestParam String jobnumber,
            @RequestParam String tagName
    ) throws Exception {
        babSensorLoginRecordService.changeUser(jobnumber, tagName);
        return "success";
    }

    @RequestMapping(value = "/stationComplete", method = {RequestMethod.POST})
    @ResponseBody
    protected String stationComplete(
            @RequestParam int bab_id,
            @RequestParam String tagName,
            @RequestParam String jobnumber
    ) {
        Bab b = babService.findByPrimaryKey(bab_id);
        BabSettingHistory setting = babSettingHistoryService.findProcessingByTagName(tagName);
        checkArgument(setting != null, "找不到該站使用者");
        checkArgument(setting.getLastUpdateTime() == null, "感應器已經關閉");
        checkStation(b, setting.getStation());

        if (setting.getStation() == b.getPeople()) { // if the station is the last station
            babService.closeBab(b);
//                            Endpoint6.syncAndEcho();
        } else {
            babService.stationComplete(b, setting);
        }
        
        return "success";
    }

    private void checkStation(Bab b, int station) {
        checkArgument(station <= b.getPeople(), "所在站別大於本工單所輸入的人數，請重新確認");
    }

}

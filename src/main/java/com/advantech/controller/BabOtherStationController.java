/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.controller;

import com.advantech.model.Bab;
import com.advantech.model.BabAlarmHistory;
import com.advantech.model.BabPreAssyPcsRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.ReplyStatus;
import com.advantech.service.BabAlarmHistoryService;
import com.advantech.service.BabPreAssyPcsRecordService;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.BabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static com.google.common.base.Preconditions.*;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
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
    private BabAlarmHistoryService babAlarmHistoryService;

    @Autowired
    private BabService babService;

    @Autowired
    private BabPreAssyPcsRecordService babPreAssyPcsRecordService;

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
            @RequestParam String jobnumber,
            @RequestParam(required = false) Integer pcs
    ) {
        Bab b = babService.findByPrimaryKey(bab_id);
        BabSettingHistory setting = babSettingHistoryService.findProcessingByTagName(tagName);
        checkArgument(setting != null, "找不到該站使用者");
        checkArgument(setting.getLastUpdateTime() == null, "感應器已經關閉");
        checkStation(b, setting.getStation());

        if (setting.getStation() == b.getPeople()) { // if the station is the last station
            //Save pre pcs record first
            if (b.getIspre() == 1) {
                List<BabSettingHistory> settings = babSettingHistoryService.findByBab(b);

                List<BabPreAssyPcsRecord> pcsRecords = new ArrayList();
                settings.forEach((bsh) -> {
                    pcsRecords.add(new BabPreAssyPcsRecord(bsh, pcs));
                });
                babPreAssyPcsRecordService.insert(pcsRecords);
            }
            
            babService.closeBab(b);
            
            BabAlarmHistory bah = babAlarmHistoryService.findByBab(bab_id);
            if (bah != null && bah.getTotalPcs() < 10) {
                //Get object again and set reply flag
                //Get bab again because object bab close by procedure not by update object itself
                //bab object is old, babStatus is null
                b = babService.findByPrimaryKey(bab_id);
                b.setReplyStatus(ReplyStatus.NO_NEED_TO_REPLY);
                babService.update(b);
            }
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

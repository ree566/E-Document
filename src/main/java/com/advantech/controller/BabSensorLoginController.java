/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.TagNameComparison;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.FloorService;
import com.advantech.service.TagNameComparisonService;
import static com.google.common.base.Preconditions.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng Because Line login and logout function are Deprecated Add
 * station login control to "decrease user Bab relative settings(bab people, bab
 * line...etc)"
 */
@Controller
@RequestMapping(value = "/BabSensorLoginController")
public class BabSensorLoginController {

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public String login(
            @RequestParam String tagName,
            @RequestParam String jobnumber,
            @RequestParam(name = "floor.name") String floorName
    ) {
        List<TagNameComparison> l = tagNameComparisonService.findByFloorName(floorName);
        TagNameComparison sensor = l.stream()
                .filter(t -> t.getId().getLampSysTagName().getName().equals(tagName))
                .findFirst().orElse(null);
        checkArgument(sensor != null, "Can't found sensor named " + tagName);
        babSensorLoginRecordService.insert(new BabSensorLoginRecord(sensor.getId().getLampSysTagName(), jobnumber));
        return "success";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    public String logout(
            @RequestParam String tagName,
            @RequestParam String jobnumber
    ) {
        BabSensorLoginRecord loginRecord = babSensorLoginRecordService.findBySensor(tagName);
        checkArgument(loginRecord != null, "Can't found login record in this sensor " + tagName);
        checkArgument(jobnumber.equals(loginRecord.getJobnumber()), "Record user is not matches " + jobnumber);
        babSensorLoginRecordService.delete(loginRecord);
        return "success";
    }

    @RequestMapping(value = "/checkStation", method = {RequestMethod.GET})
    @ResponseBody
    public boolean checkStation(
            @RequestParam String tagName,
            @RequestParam boolean isFirstStation
    ) {
        BabSettingHistory setting = babSettingHistoryService.findProcessingByTagName(tagName);
        if (setting == null) {
            return true;
        } else {
            if (isFirstStation) {
                checkArgument(setting.getStation() == 1,
                        "無法切換站別，此Sensor尚在上套的工單站別 " + setting.getStation() + " 進行中");
            } else {
                checkArgument(setting.getStation() != 1,
                        "無法切換站別，此Sensor尚在上套的工單站別 " + setting.getStation() + " 進行中");
            }
            return true;
        }
    }

}

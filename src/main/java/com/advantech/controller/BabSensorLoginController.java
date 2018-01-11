/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.SensorTransform;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.SensorTransformService;
import static com.google.common.base.Preconditions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng 
 * Because Line login and logout function are Deprecated
 * Add station login control to "decrease user Bab relative settings(bab people, bab line...etc)"
 */
@Controller
@RequestMapping(value = "/BabSensorLoginController")
public class BabSensorLoginController {
    
    @Autowired
    private SensorTransformService sensorTransformService;
    
    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;
    
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public String login(
            @RequestParam String jobnumber,
            @RequestParam String tagName
    ) {
        SensorTransform sensor = sensorTransformService.findByPrimaryKey(tagName);
        checkArgument(sensor != null, "Can't found sensor named " + tagName);
        babSensorLoginRecordService.insert(new BabSensorLoginRecord(sensor, jobnumber));
        return "success";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    public String logout(
            @RequestParam String jobnumber,
            @RequestParam String tagName
    ) {
        BabSensorLoginRecord loginRecord = babSensorLoginRecordService.findBySensor(tagName);
        checkArgument(loginRecord != null, "Can't found login record in this sensor " + tagName);
        checkArgument(jobnumber.equals(loginRecord.getJobnumber()), "Record user is not matches " + jobnumber);
        babSensorLoginRecordService.delete(loginRecord);
        return "success";
    }

}

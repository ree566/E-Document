/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.db1.BabPassStationErrorRecord;
import com.advantech.model.db1.BabPassStationRecord;
import com.advantech.service.db1.BabPassStationErrorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabPassStationErrorRecordController")
public class BabPassStationErrorRecordController {

    @Autowired
    private BabPassStationErrorRecordService service;

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    protected String insert(
            @CookieValue(required = true) String userInfo,
            @ModelAttribute BabPassStationRecord record,
            @RequestParam(required = false) String reason
    ) {
        BabPassStationErrorRecord error = new BabPassStationErrorRecord();
        error.setBabPassStationRecord(record);
        error.setReason(reason);
        service.insert(error);
        return "success";
    }

}

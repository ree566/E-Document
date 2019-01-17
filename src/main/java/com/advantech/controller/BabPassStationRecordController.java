/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Bab;
import com.advantech.model.BabPassStationRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.service.BabPassStationRecordService;
import com.advantech.service.BabSettingHistoryService;
import static com.google.common.base.Preconditions.checkArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabPassStationRecordController")
public class BabPassStationRecordController {

    @Autowired
    private BabPassStationRecordService babPassStationRecordService;

    @Autowired
    private BabSettingHistoryService settingHistoryService;

    @RequestMapping(value = "/redirect", method = {RequestMethod.GET})
    protected ModelAndView findBabAndredirect(
            @CookieValue(required = true) String userInfo,
            @RequestParam String tagName
    ) {
        BabSettingHistory setting = settingHistoryService.findProcessingByTagName(tagName);
        checkArgument(setting != null, "Can't find processing bab");
        ModelAndView mav = new ModelAndView("barcode_input");
        mav.addObject("tagName", tagName);
        mav.addObject("bab_id", setting.getBab().getId());
        return mav;
    }

    @RequestMapping(value = "/findLastProcessByTagName", method = {RequestMethod.GET})
    @ResponseBody
    protected BabPassStationRecord findLastProcessByTagName(
            @CookieValue(required = true) String userInfo,
            @RequestParam String tagName
    ) {
        return babPassStationRecordService.findLastProcessingByTagName(tagName);
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    protected int insert(
            @CookieValue(required = true) String userInfo,
            @RequestParam String barcode,
            @RequestParam String tagName,
            @ModelAttribute Bab bab
    ) {
        checkArgument(barcode.length() >= 5, "Barcode length can't under five character!");
        int count = babPassStationRecordService.checkStationInfoAndInsert(bab, tagName, barcode);
        return count;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@RequestParam String barcode, @ModelAttribute Bab bab) {
        return "success";
    }
}

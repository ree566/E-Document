/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.Bab;
import com.advantech.model.TagNameComparison;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.TagNameComparisonService;
import static com.google.common.base.Preconditions.checkArgument;
import org.json.JSONObject;
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
@RequestMapping(value = "/BabSettingHistoryController")
public class BabSettingHistoryController {

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private TagNameComparisonService tagNameService;

    @RequestMapping(value = "/findByBab", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findBabSettingHistory(@ModelAttribute Bab bab) {
        return new DataTableResponse(babSettingHistoryService.findByBab(bab));
    }

    @RequestMapping(value = "/findWithMaxBalanceOrMinAlarmPercent", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findBabSettingHistory(
            @RequestParam String po,
            @CookieValue(required = true) String userInfo,
            @RequestParam(required = false, defaultValue = "true") Boolean findWithCurrentLine,
            @RequestParam(required = false, defaultValue = "false") Boolean findWithBalance,
            @RequestParam(required = false, defaultValue = "false") Boolean findWithAlarmPercent,
            @RequestParam(required = false, defaultValue = "true") Boolean isAboveMinPcs
    ) {
        int line_id = 0;
        if (findWithCurrentLine) {
            JSONObject obj = new JSONObject(userInfo);
            String tagName = obj.getString("tagName");
            checkArgument(tagName != null && !"".equals(tagName));
            TagNameComparison tag = tagNameService.findByLampSysTagName(tagName);
            checkArgument(tagName != null);
            line_id = tag.getLine().getId();
        }
        return new DataTableResponse(babSettingHistoryService.findAll(po, line_id,
                findWithBalance, findWithAlarmPercent, isAboveMinPcs ? 10 : 0));
    }

}

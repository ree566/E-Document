/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.service.BabPcsDetailHistoryService;
import com.advantech.service.SqlViewService;
import static com.google.common.base.Preconditions.checkArgument;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
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
@RequestMapping(value = "/BabPcsDetailHistoryController")
public class BabPcsDetailHistoryController {

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private BabPcsDetailHistoryService babPcsDetailHistoryService;

    @RequestMapping(value = "/findByBab", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findByBab(@ModelAttribute Bab bab) {
        checkArgument(bab != null);
        BabStatus status = bab.getBabStatus();
        if (status == null) {
            return new DataTableResponse(sqlViewService.findSensorStatus(bab.getId()));
        } else {
            return new DataTableResponse(babPcsDetailHistoryService.findByBab(bab.getId()));
        }
    }

    @RequestMapping(value = "/findReport", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findReport(
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String lineType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {

        if (modelName == null || "".equals(modelName.trim())) {
            modelName = null;
        }

        if ("-1".equals(lineType)) {
            lineType = null;
        }

        //modelName 或 lineType 為空時一定要設定 startDate & endDate
        checkArgument(!((modelName == null || lineType == null) && (startDate == null || endDate == null)),
                "\"ModelName\"或\"類別\"為空時，一定要設定\"startDate\"&\"endDate\"");

        if (modelName == null && startDate != null && endDate != null) {
            checkArgument(Weeks.weeksBetween(startDate.toLocalDate(), endDate.toLocalDate()).getWeeks()<= 2,
                    "日期區間不得超過2週");
        }

        return new DataTableResponse(sqlViewService.findBabPcsDetail(modelName, lineType, startDate, endDate));
    }
}

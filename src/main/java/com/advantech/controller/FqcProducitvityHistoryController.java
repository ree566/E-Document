/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 排定抓取資料的排程
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.service.FqcProducitvityHistoryService;
import java.io.*;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@Controller
@RequestMapping("/FqcProducitvityHistoryController")
public class FqcProducitvityHistoryController {

    private static final Logger log = LoggerFactory.getLogger(FqcProducitvityHistoryController.class);

    @Autowired
    private FqcProducitvityHistoryService fqcProducitvityHistoryService;

    @RequestMapping(value = "/findByDate", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate
    ) throws IOException, SchedulerException {

        return new DataTableResponse(fqcProducitvityHistoryService.findComplete(startDate, endDate));

    }
}

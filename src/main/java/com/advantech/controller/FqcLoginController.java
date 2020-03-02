/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 排定抓取資料的排程
 */
package com.advantech.controller;

import com.advantech.model.db1.FqcLine;
import com.advantech.model.db1.FqcLoginRecord;
import com.advantech.service.db1.FqcLineService;
import com.advantech.service.db1.FqcLoginRecordService;
import static com.google.common.base.Preconditions.checkArgument;
import java.io.*;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/FqcLoginController")
public class FqcLoginController {

    private static final Logger log = LoggerFactory.getLogger(FqcLoginController.class);

    @Autowired
    private FqcLineService fqcLineService;

    @Autowired
    private FqcLoginRecordService fqcLoginRecordService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public String insert(
            @RequestParam(name = "fqcLine.id") int fqcLine_id,
            @RequestParam String jobnumber
    ) throws IOException, SchedulerException {

        FqcLine fqcLine = fqcLineService.findByPrimaryKey(fqcLine_id);
        checkArgument(fqcLine != null, "Can't find this fqcLine in setting");

        FqcLoginRecord record = new FqcLoginRecord(fqcLine, jobnumber);
        fqcLoginRecordService.insert(record);

        return "success";

    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(
            @RequestParam(name = "fqcLine.id") int fqcLine_id,
            @RequestParam String jobnumber
    ) throws IOException, SchedulerException {

        FqcLoginRecord fqcLoginRecord = fqcLoginRecordService.findByFqcLine(fqcLine_id);
        checkArgument(fqcLoginRecord != null, "Can't find login record in setting");
        checkArgument(jobnumber.equals(fqcLoginRecord.getJobnumber()), "Jobnumber is not match in login record");
        fqcLoginRecordService.delete(fqcLoginRecord);
        
        return "success";
    }

}

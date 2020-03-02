/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得測試線別效率紀錄
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import static com.advantech.helper.SecurityPropertiesUtils.retrieveAndCheckUserInSession;
import com.advantech.model.db1.ReplyStatus;
import com.advantech.model.db1.TestRecord;
import com.advantech.model.db1.TestRecordRemark;
import com.advantech.model.db1.User;
import com.advantech.service.db1.TestRecordRemarkService;
import com.advantech.service.db1.TestRecordService;
import java.io.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/TestRecordController")
public class TestRecordController {

    @Autowired
    private TestRecordService testRecordService;

    @Autowired
    private TestRecordRemarkService testRecordRemarkService;

    @RequestMapping(value = "/findByDate", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam boolean unReplyOnly
    ) throws IOException {
        return new DataTableResponse(testRecordService.findByDate(startDate, endDate, unReplyOnly));
    }

    @RequestMapping(value = "/findRemark", method = {RequestMethod.GET})
    @ResponseBody
    protected TestRecordRemark findRemark(@RequestParam int recordId) {
        return testRecordRemarkService.findByTestRecord(recordId);
    }

    @RequestMapping(value = "/updateRemark", method = {RequestMethod.POST})
    @ResponseBody
    protected boolean updateRemark(
            @RequestParam int recordId,
            @RequestParam String remark
    ) {
        User user = retrieveAndCheckUserInSession();

        TestRecord record = testRecordService.findByPrimaryKey(recordId);

        if (record.getReplyStatus() == ReplyStatus.UNREPLIED) {
            TestRecordRemark tr = new TestRecordRemark();
            tr.setTestRecord(record);
            tr.setRemark(remark);
            tr.setUser(user);
            testRecordRemarkService.insert(tr);
        } else if (record.getReplyStatus() == ReplyStatus.REPLIED) {
            TestRecordRemark tr = testRecordRemarkService.findByTestRecord(recordId);
            tr.setRemark(remark);
            testRecordRemarkService.update(tr);
        }

        return true;
    }
}

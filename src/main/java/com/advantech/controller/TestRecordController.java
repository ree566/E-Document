/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得測試線別效率紀錄
 */
package com.advantech.controller;

import com.advantech.model.TestRecord;
import com.advantech.service.TestRecordService;
import java.io.*;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/TestRecordController")
public class TestRecordController {

    @Autowired
    private TestRecordService testRecordService;

    @RequestMapping(value = "/findByDate", method = {RequestMethod.POST})
    protected List<TestRecord> findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate
    ) throws IOException {
        return testRecordService.findByDate(startDate, endDate);
    }
}

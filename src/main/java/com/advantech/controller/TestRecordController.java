/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得測試線別效率紀錄
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.service.TestRecordService;
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

    @RequestMapping(value = "/findByDate", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate
    ) throws IOException {
        return new DataTableResponse(testRecordService.findByDate(startDate, endDate));
    }
}

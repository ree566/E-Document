/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.TagNameComparison;
import com.advantech.service.FbnService;
import com.advantech.service.TagNameComparisonService;
import java.util.List;
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
@RequestMapping(value = "/SensorController")
public class SensorController {

    @Autowired
    private TagNameComparisonService tagService;
    
    @Autowired
    private FbnService fbnService;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected List<TagNameComparison> findAll() {
        return tagService.findAll();
    }
    
    @RequestMapping(value = "/findSensorData", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findSensorData(
            @RequestParam String tagName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") DateTime endDate
    ) {
        return new DataTableResponse(fbnService.findByTagNameAndDate(tagName, startDate, endDate));
    }
}

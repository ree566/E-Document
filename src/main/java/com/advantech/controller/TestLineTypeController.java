/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試頁面儲存用
 */
package com.advantech.controller;

import com.advantech.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/TestLineTypeController")
public class TestLineTypeController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public String login(
            @RequestParam String jobnumber,
            @RequestParam int tableNo
    ) {
        testService.insert(tableNo, jobnumber);
        return "success";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    public String logout(@RequestParam String jobnumber) {
        testService.delete(jobnumber);
        return "success";
    }

    @RequestMapping(value = "/changeDesk", method = {RequestMethod.POST})
    @ResponseBody
    public String changeDesk(
            @RequestParam String jobnumber,
            @RequestParam int tableNo
    ) {
        testService.changeDeck(jobnumber);
        return "success";
    }
}

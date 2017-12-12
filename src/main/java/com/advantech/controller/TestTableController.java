/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢測試桌狀態
 */
package com.advantech.controller;

import com.advantech.model.TestTable;
import com.advantech.service.TestLineTypeFacade;
import com.advantech.service.TestTableService;
import java.util.List;
import java.util.Map;
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
@RequestMapping(value = "/TestTableController")
public class TestTableController {

    @Autowired
    private TestTableService testTableService;
    
    @Autowired
    private TestLineTypeFacade ts;

    @RequestMapping(value = "/findBySitefloor", method = {RequestMethod.GET})
    @ResponseBody
    protected List<TestTable> findBySitefloor(@RequestParam int sitefloor) {
        return testTableService.findBySitefloor(sitefloor);
    }
    
    @RequestMapping(value = "/findUserNotLogin", method = {RequestMethod.GET})
    @ResponseBody
    protected Map findUserNotLogin(){
        return ts.getPEOPLE_NOT_MATCH();
    }

}

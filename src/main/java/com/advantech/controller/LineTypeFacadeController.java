/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Total頁面顯示(XML查詢跟目錄Total) 查詢組裝包裝、測試相關資訊後轉成XML丟給前端用
 */
package com.advantech.controller;

import com.advantech.service.BabLineTypeFacade;
import com.advantech.service.TestLineTypeFacade;
import java.io.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/LineTypeFacadeController")
public class LineTypeFacadeController {

    @Autowired
    private TestLineTypeFacade ts;
    
    @Autowired
    private BabLineTypeFacade bs;
    
    @RequestMapping(value = "/findBabProcessResult", method = {RequestMethod.GET})
    @ResponseBody
    protected String findBabProcessResult(){
        return bs.getJSONObject().toString();
    }
    
    @RequestMapping(value = "/findTestProcessResult", method = {RequestMethod.GET})
    @ResponseBody
    protected String findTestProcessResult(){
        return ts.getJSONObject().toString();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.JsonHelper;
import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import com.advantech.response.JqGridResponse;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@SessionAttributes({"user"})
public class SheetViewController {

    private static final Logger log = LoggerFactory.getLogger(SheetViewController.class);

    @Autowired
    private SheetViewService sheetViewService;
    
    @Autowired
    private WorktimeService worktimeService;

    @ResponseBody
    @RequestMapping(value = "/getSheetView.do", method = {RequestMethod.POST})
    public JqGridResponse getSheetView(@ModelAttribute PageInfo info, @ModelAttribute("user") Identit user, BindingResult errors) throws JsonProcessingException, JsonProcessingException, IOException {
        if (errors.hasErrors()) {
            // error handling code goes here.
            log.error(new Gson().toJson(errors.getFieldErrors()));
        }

        List l = sheetViewService.findAll(info);
        
        JqGridResponse viewResp = getResponseObject(l, info);
        return viewResp;
    }
    
    private JqGridResponse getResponseObject(List l, PageInfo info) {
        JqGridResponse viewResp = new JqGridResponse();
        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;
        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());
        return viewResp;
    }

    @ResponseBody
    @RequestMapping(value = "/generateExcel.do", method = {RequestMethod.GET})
    public ModelAndView generateExcel() {
        // create some sample data
        List<SheetView> l = sheetViewService.findAll();
        return new ModelAndView("ExcelRevenueSummary", "revenueData", l);
    }

    @ExceptionHandler(HttpSessionRequiredException.class)
//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The session has expired")
    public ResponseEntity handleSessionExpired() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("The session has expired.");
    }

    @ResponseBody
    @RequestMapping(value = "/sheetViewTestMode.do", method = {RequestMethod.POST})
    public JqGridResponse getSheetViewTestMode(@ModelAttribute PageInfo info, @ModelAttribute("user") Identit user) {
        
        List l = new ArrayList();
        JqGridResponse viewResp = new JqGridResponse();

        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;

        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());

        return viewResp;
    }
}

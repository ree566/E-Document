/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.Identit;
import com.advantech.response.JqGridResponse;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@SessionAttributes({"user"})
public class TestRequestController {

    @Autowired
    private WorktimeService worktimeService;

    @ResponseBody
    @RequestMapping(value = "/getSomeTable.do", method = {RequestMethod.GET, RequestMethod.POST})
    public JqGridResponse getSheetView(@ModelAttribute PageInfo info, @ModelAttribute("user") Identit user) {
        return getResponseObject(worktimeService.findAll(info), info);
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

    //The request param name indexOf('name') == 1 is selected box which has one to many relationship.
    @RequestMapping(value = "/testRequest.do", method = {RequestMethod.POST})
    public ResponseEntity testRequest(
            @ModelAttribute("user") Identit user,
            @ModelAttribute Flow flow,
            HttpServletRequest req) throws ServletException, IOException {

        this.showParams(req);

        System.out.println(new Gson().toJson(flow));

        return ResponseEntity.status(HttpStatus.CREATED).body("testing");
    }

    private void showParams(HttpServletRequest req) throws ServletException, IOException {
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }
    }
}

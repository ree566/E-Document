/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@SessionAttributes({"user"})
public class TestController {

    @Autowired
    private AuditService auditService;

    @ResponseBody
    @RequestMapping(value = "/test.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List test(
            Model model,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @ModelAttribute PageInfo info,
            @ModelAttribute("user") Identit user) {

        info.setRows(1000);
        List l = auditService.findByDate(Worktime.class, info, startDate, endDate);
//
//        System.out.println(l.size());
//        
//        model.addAttribute("data", l);
//        model.addAttribute("startDate", startDate);
//        model.addAttribute("endDate", endDate);

//        return "forward:test.jsp";
        return l;
    }
}

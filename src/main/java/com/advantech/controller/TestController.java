/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/testCtrl")
public class TestController {

    @Autowired
    private AuditService auditService;
    
    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "hi";
    }
    
    @ResponseBody
    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public String test1() {
        return "hi1";
    }
    
    @ResponseBody
    @RequestMapping(value = "/test2/{id}", method = RequestMethod.GET)
    public List test2(@PathVariable int id) {
        return auditService.findByPrimaryKey(Worktime.class, id);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.Identit;
import com.advantech.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class TestController {

    @Autowired
    private FlowService flowService;

    @ResponseBody
    @RequestMapping(value = "/test.do/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public Flow test(
            @PathVariable int id,
            @ModelAttribute("user") Identit user) {

        return flowService.findByPrimaryKey(1);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.User;
import com.advantech.service.LineService;
import com.advantech.service.ModelSopRemarkService;
import static com.google.common.base.Preconditions.checkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping(value = "/ModelSopRemarkController")
public class ModelSopRemarkController {

    @Autowired
    private ModelSopRemarkService modelSopRemarkService;
    
    @Autowired
    private LineService lineService;

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    protected String insert(
            @RequestParam String modelName,
            @RequestParam(required = false) String remark,
            @RequestParam(name = "stations[]") int[] stations,
            @RequestParam(name = "sopNames[]") String[] sopNames,
            @RequestParam(name = "sopPages[]") String[] sopPages
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        checkState(!(auth instanceof AnonymousAuthenticationToken), "查無登入紀錄，請重新登入");
        User user = (User) auth.getPrincipal();
        
//        Line line = lineService.f
        
//        modelSopRemarkService.insert(pojo);
        return "success";
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    protected String update() {
        return "success";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete() {
        return "success";
    }
}

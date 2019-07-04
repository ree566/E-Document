/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.PreAssyModuleStandardTime;
import com.advantech.model.User;
import com.advantech.service.PreAssyModuleStandardTimeService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng Line login and logout function are Deprecated
 */
@Controller
@RequestMapping(value = "/PreAssyModuleStandardTimeController")
public class PreAssyModuleStandardTimeController {

    @Autowired
    private PreAssyModuleStandardTimeService service;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findAll(HttpServletRequest request) {
        List l;
        l = service.findAll();
//        if (request.isUserInRole("ROLE_ADMIN")) {
//            l = service.findAll();
//        } else {
//            User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
//            l = service.findByFloor(user.getFloor());
//        }
        return new DataTableResponse(l);
    }

    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveOrUpdate(@Valid @ModelAttribute PreAssyModuleStandardTime pojo) {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        service.checkIsPreAssyModuleTypeExists(pojo, user.getFloor());
        if (pojo.getId() == 0) {
            service.insert(pojo);
        } else {
            service.update(pojo);
        }
        return "success";
    }

    @RequestMapping(value = "/saveBySeries", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveBySeries(@RequestParam String baseModelName, @RequestParam String targetModelName) throws CloneNotSupportedException {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        service.insertBySeries(baseModelName, targetModelName, user.getFloor());
        return "success";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@RequestParam int id) {
        PreAssyModuleStandardTime pojo = service.findByPrimaryKey(id);
        service.delete(pojo);
        return "success";
    }

}

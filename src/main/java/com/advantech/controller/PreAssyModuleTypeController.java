/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.db1.PreAssyModuleType;
import com.advantech.service.db1.PreAssyModuleTypeService;
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
@RequestMapping(value = "/PreAssyModuleTypeController")
public class PreAssyModuleTypeController {

    @Autowired
    private PreAssyModuleTypeService service;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findAll() {
        return new DataTableResponse(service.findAll());
    }
    
    
    @RequestMapping(value = "/findByModelName", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findByModelName(@RequestParam String modelName) {
        return new DataTableResponse(service.findByModelName(modelName));
    }

    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveOrUpdate(@Valid @ModelAttribute PreAssyModuleType pojo) {
//        service.checkIsPreAssyModuleTypeExists(pojo);
        if (pojo.getId() == 0) {
            service.insert(pojo);
        } else {
            service.update(pojo);
        }
        return "success";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@RequestParam int id) {
        PreAssyModuleType pojo = service.findByPrimaryKey(id);
        service.delete(pojo);
        return "success";
    }

}

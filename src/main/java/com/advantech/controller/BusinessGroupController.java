/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.BusinessGroup;
import com.advantech.jqgrid.JqGridResponse;
import com.advantech.service.BusinessGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BusinessGroup")
public class BusinessGroupController extends CrudController<BusinessGroup> {

    @Autowired
    private BusinessGroupService businessGroupService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse read(PageInfo info) {
        return toJqGridResponse(businessGroupService.findAll(info), info);
    }

    @ResponseBody
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity insert(BusinessGroup businessGroup, BindingResult bindingResult) {
        String modifyMessage = businessGroupService.insert(businessGroup) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(BusinessGroup businessGroup, BindingResult bindingResult) {
        String modifyMessage = businessGroupService.update(businessGroup) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity delete(int id) {
        String modifyMessage = businessGroupService.delete(id) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }
}

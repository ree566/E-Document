/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.service.BabPcsDetailHistoryService;
import com.advantech.service.SqlViewService;
import static com.google.common.base.Preconditions.checkArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabPcsDetailHistoryController")
public class BabPcsDetailHistoryController {

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private BabPcsDetailHistoryService babPcsDetailHistoryService;

    @RequestMapping(value = "/findByBab", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findByBab(@ModelAttribute Bab bab) {
        checkArgument(bab != null);
        BabStatus status = bab.getBabStatus();
        if (status == null) {
            return new DataTableResponse(sqlViewService.findSensorStatus(bab.getId()));
        } else {
            return new DataTableResponse(babPcsDetailHistoryService.findByBab(bab.getId()));
        }
    }

}

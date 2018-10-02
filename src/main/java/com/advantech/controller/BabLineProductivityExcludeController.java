/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Bab;
import com.advantech.model.BabLineProductivityExclude;
import com.advantech.service.BabLineProductivityExcludeService;
import com.advantech.service.BabService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/BabLineProductivityExcludeController")
public class BabLineProductivityExcludeController {

    @Autowired
    private BabLineProductivityExcludeService babLineProductivityExcludeService;

    @Autowired
    private BabService babService;

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    public String insert(@RequestParam(name = "id[]") Integer[] ids) {
        List<Bab> l = babService.findByPrimaryKeys(ids);
        if (!l.isEmpty()) {
            List<BabLineProductivityExclude> ignoreBabs = new ArrayList();
            l.forEach((b) -> {
                ignoreBabs.add(new BabLineProductivityExclude(b));
            });
            babLineProductivityExcludeService.insert(ignoreBabs);
        }
        return "success";
    }

}

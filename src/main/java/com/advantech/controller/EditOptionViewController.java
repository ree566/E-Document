/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.UserService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.advantech.service.UnitService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EditOptionViewController {

    @Autowired
    private FloorService floorService;

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private PendingService pendingService;
    
    @Autowired
    private UnitService unitService;

    @ResponseBody
    @RequestMapping(value = "/floorOption.do", method = {RequestMethod.GET})
    public List getFloorOption() {
        return floorService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/userOption.do/{unitName}", method = {RequestMethod.GET})
    public List getUserOption(@PathVariable(value = "unitName") final String unitName) {
        return userService.findByUnitName(unitName);
    }

    @ResponseBody
    @RequestMapping(value = "/typeOption.do", method = {RequestMethod.GET})
    public List getTypeOption() {
        return typeService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/flowOption.do/{flowGroupId}", method = {RequestMethod.GET})
    public List getFlowOption(@PathVariable(value = "flowGroupId") final int flowGroupId) {
        return flowService.findByFlowGroup(flowGroupId);
    }

    @ResponseBody
    @RequestMapping(value = "/preAssyOption.do", method = {RequestMethod.GET})
    public List getPreAssyOption() {
        return preAssyService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/pendingOption.do", method = {RequestMethod.GET})
    public List getPendingOption() {
        return pendingService.findAll();
    }
    
    @ResponseBody
    @RequestMapping(value = "/unitOption.do", method = {RequestMethod.GET})
    public List getUserTypeOption() {
        return unitService.findAll();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.service.FloorService;
import com.advantech.service.FlowGroupService;
import com.advantech.service.FlowService;
import com.advantech.service.UserService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.advantech.service.UnitService;
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
@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_GUEST"})
@RequestMapping(value = "/SelectOption")
public class WorktimeSelectOptionController {

    @Autowired
    private FloorService floorService;

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FlowService flowService;
    
    @Autowired
    private FlowGroupService flowGroupService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private UnitService unitService;

    @ResponseBody
    @RequestMapping(value = "/floor", method = {RequestMethod.GET})
    public List getFloorOption() {
        return floorService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/user/{unitName}", method = {RequestMethod.GET})
    public List getUserOption(@PathVariable(value = "unitName") final String unitName) {
        return userService.findByUnitName(unitName);
    }

    @ResponseBody
    @RequestMapping(value = "/type", method = {RequestMethod.GET})
    public List getTypeOption() {
        return typeService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/flow/{flowGroupId}", method = {RequestMethod.GET})
    public List getFlowOption(@PathVariable(value = "flowGroupId") final int flowGroupId) {
        return flowService.findByFlowGroup(flowGroupId);
    }
    
    @ResponseBody
    @RequestMapping(value = "/flow", method = {RequestMethod.GET})
    public List getAllFlowOption() {
        return flowService.findAll();
    }
    
    @ResponseBody
    @RequestMapping(value = "/flowGroup", method = {RequestMethod.GET})
    public List getFlowGroupOption() {
        return flowGroupService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/preAssy", method = {RequestMethod.GET})
    public List getPreAssyOption() {
        return preAssyService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/pending", method = {RequestMethod.GET})
    public List getPendingOption() {
        return pendingService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/unit", method = {RequestMethod.GET})
    public List getUserTypeOption() {
        return unitService.findAll();
    }
}

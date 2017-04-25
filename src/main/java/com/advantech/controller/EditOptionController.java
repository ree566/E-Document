/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Identit;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.IdentitService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.advantech.service.UserTypeService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class EditOptionController {

    @Autowired
    private FloorService floorService;

    @Autowired
    private IdentitService identitService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private PendingService pendingService;
    
    @Autowired
    private UserTypeService userTypeService;

    @ResponseBody
    @RequestMapping(value = "/floorOption.do", method = {RequestMethod.GET})
    public List getFloorOption() {
        return floorService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/identitOption.do/{unitName}", method = {RequestMethod.GET})
    public List getIdentitOption(@PathVariable(value = "unitName") final String unitName) {
        return identitService.findByUserTypeName(unitName);
    }

    @ResponseBody
    @RequestMapping(value = "/typeOption.do", method = {RequestMethod.GET})
    public List getTypeOption() {
        return typeService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/flowOption.do", method = {RequestMethod.GET})
    public List getFlowOption() {
        return flowService.findAll();
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
    @RequestMapping(value = "/userTypeOption.do", method = {RequestMethod.GET})
    public List getUserTypeOption() {
        return userTypeService.findAll();
    }
}

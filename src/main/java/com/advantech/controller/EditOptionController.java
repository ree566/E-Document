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
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @ResponseBody
    @RequestMapping(value = "/floorOption.do", method = {RequestMethod.GET})
    public String getFloorOption() {

        List<Floor> l = floorService.findAll();
        Map m = new HashMap();

        for (Floor f : l) {
            m.put(f.getId(), f.getName());
        }

        String optionString = convertToOptions(m);

        return optionString;
    }

    @ResponseBody
    @RequestMapping(value = "/identitOption.do", method = {RequestMethod.GET})
    public String getIdentitOption(@RequestParam String unitName) {

        List<Identit> l = identitService.findByUserTypeName(unitName);
        Map m = new HashMap();
        for (Identit i : l) {
            m.put(i.getId(), i.getUsername());
        }

        String optionString = convertToOptions(m);

        return optionString;
    }

    @ResponseBody
    @RequestMapping(value = "/typeOption.do", method = {RequestMethod.GET})
    public String getTypeOption() {

        List<Type> l = typeService.findAll();
        Map m = new HashMap();
        for (Type t : l) {
            m.put(t.getId(), t.getName());
        }

        String optionString = convertToOptions(m);

        return optionString;
    }

    @ResponseBody
    @RequestMapping(value = "/flowOption.do", method = {RequestMethod.GET})
    public String getFlowOption() {

        List<Flow> l = flowService.findAll();
        Map m = new HashMap();
        m.put(0, "empty");
        for (Flow f : l) {
            m.put(f.getId(), f.getName());
        }

        String optionString = convertToOptions(m);

        return optionString;
    }

    @ResponseBody
    @RequestMapping(value = "/preAssyOption.do", method = {RequestMethod.GET})
    public String getPreAssyOption() {

        List<PreAssy> l = preAssyService.findAll();
        Map m = new HashMap();
        m.put(0, "empty");
        for (PreAssy p : l) {
            m.put(p.getId(), p.getName());
        }

        String optionString = convertToOptions(m);

        return optionString;
    }
    
    @ResponseBody
    @RequestMapping(value = "/pendingOption.do", method = {RequestMethod.GET})
    public String getPendingOption() {

        List<Pending> l = pendingService.findAll();
        Map m = new HashMap();

        for (Pending p : l) {
            m.put(p.getId(), p.getName());
        }

        String optionString = convertToOptions(m);

        return optionString;
    }

    private String convertToOptions(Map m) {
        String optionString = new Gson().toJson(m);
        optionString = optionString.replaceAll("[{}\"]", "").replaceAll(",", ";");
        return optionString;
    }
}

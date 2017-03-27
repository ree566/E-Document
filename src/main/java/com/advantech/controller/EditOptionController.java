/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Floor;
import com.advantech.model.Identit;
import com.advantech.service.FloorService;
import com.advantech.service.IdentitService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @ResponseBody
    @RequestMapping(value = "/floorOption.do", method = {RequestMethod.GET})
    public String getFloorOption() {

        List<Floor> l = (List<Floor>) floorService.findAll();
        Map m = new HashMap();

        for (Floor f : l) {
            m.put(f.getId(), f.getName());
        }

        String optionString = new Gson().toJson(m);
        optionString = optionString.replaceAll("[{}\"]", "").replaceAll(",", ";");

        return optionString;
    }

    @ResponseBody
    @RequestMapping(value = "/identitOption.do", method = {RequestMethod.GET})
    public String getIdentitOption() {

        List<Identit> l = (List<Identit>) identitService.findAll();
        Map m = new HashMap();
        m.put(0, "empty");
        for (Identit i : l) {
            m.put(i.getId(), i.getName());
        }

        String optionString = new Gson().toJson(m);
        optionString = optionString.replaceAll("[{}\"]", "").replaceAll(",", ";");

        return optionString;
    }
}

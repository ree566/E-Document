/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import static com.advantech.helper.SecurityPropertiesUtils.*;
import com.advantech.model.Line;
import com.advantech.model.LineType;
import com.advantech.model.User;
import com.advantech.service.LineService;
import com.advantech.service.LineTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng Line login and logout function are Deprecated
 */
@Controller
@RequestMapping(value = "/BabLineController")
public class BabLineController {

    @Autowired
    private LineService lineService;

    @Autowired
    private LineTypeService lineTypeService;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Line> findAll(@RequestParam(required = false) String sitefloor) {
        return sitefloor != null ? lineService.findBySitefloor(sitefloor) : lineService.findAll();
    }

    @RequestMapping(value = "/findWithLineType", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Line> findWithLineType() {
        return lineService.findWithLineType();
    }

    @RequestMapping(value = "/findLineType", method = {RequestMethod.GET})
    @ResponseBody
    protected List<LineType> findLineType() {
        return lineTypeService.findAll();
    }

    @RequestMapping(value = "/findByUser", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Line> findByUser() {
        User user = retrieveAndCheckUserInSession();
        return lineService.findByUser(user);
    }

}

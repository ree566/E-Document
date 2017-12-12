/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.model.Line;
import com.advantech.service.LineService;
import java.io.*;
import java.util.List;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Line> findAll(@RequestParam(required = false) Integer sitefloor) {
        return sitefloor != null ? lineService.findBySitefloor(sitefloor) : lineService.findAll();
    }

}

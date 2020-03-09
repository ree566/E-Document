/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.model.db1.FqcLine;
import com.advantech.service.db1.FqcLineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng FqcLine login and logout function are Deprecated
 */
@Controller
@RequestMapping(value = "/FqcLineController")
public class FqcLineController {

    @Autowired
    private FqcLineService fqcLineService;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected List<FqcLine> findAll(@RequestParam(required = false) String sitefloor) {
        return sitefloor != null ? fqcLineService.findBySitefloor(sitefloor) : fqcLineService.findAll();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 排定抓取資料的排程
 */
package com.advantech.controller;

import com.advantech.converter.BabStatusControllerConverter;
import com.advantech.model.BabStatus;
import com.advantech.model.Fqc;
import com.advantech.model.FqcProducitvityHistory;
import com.advantech.service.FqcProducitvityHistoryService;
import com.advantech.service.FqcService;
import static com.google.common.base.Preconditions.*;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng 前端給予PO, servlet 負責 sched the new polling job.
 */
@Controller
@RequestMapping("/FqcController")
public class FqcController {
    
    private static final Logger log = LoggerFactory.getLogger(FqcController.class);
    
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(BabStatus.class, new BabStatusControllerConverter());
    }
    
    @Autowired
    private FqcService fqcService;
    
    @Autowired
    private FqcProducitvityHistoryService productivityService;

    //FqcLine should not be null
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    public Fqc insert(@Valid @ModelAttribute Fqc fqc) {
        fqcService.checkAndInsert(fqc);
        return fqc;
    }
    
    @RequestMapping(value = "/reconnectAbnormal", method = {RequestMethod.POST})
    @ResponseBody
    public FqcProducitvityHistory reconnectAbnormal(@Valid @RequestBody Fqc fqc) {
        FqcProducitvityHistory p = productivityService.findByFqc(fqc.getId());
        fqcService.reconnectAbnormal(fqc);
        return p;
    }
    
    @RequestMapping(value = "/findReconnectable", method = {RequestMethod.GET})
    @ResponseBody
    public Fqc findReconnectable(@RequestParam int fqcLine_id, String po) {
        List<Fqc> l = fqcService.findReconnectable(fqcLine_id, po);
        return l.isEmpty() ? new Fqc() : l.get(0);
    }

//    Save時把MES過站資料存回資料庫
    @RequestMapping(value = "/stationComplete", method = {RequestMethod.POST})
    @ResponseBody
    protected String stationComplete(
            @RequestParam(name = "fqc.id") int fqc_id,
            @RequestParam int timeCost,
            @RequestParam String remark
    ) {
        
        Fqc fqc = fqcService.findByPrimaryKey(fqc_id);
        checkArgument(fqc != null, "Can't find fqc object in database");
        
        fqc.setRemark(remark == null || "".equals(remark.trim()) ? null : remark);
        fqcService.stationComplete(fqc, timeCost);
        return "success";
    }
    
    @RequestMapping(value = "/addAbnormalReconnectableSignAndClose", method = {RequestMethod.POST})
    @ResponseBody
    protected String addAbnormalReconnectableSignAndClose(
            @RequestParam(name = "fqc.id") int fqc_id,
            @RequestParam int timeCost,
            @RequestParam String remark
    ) {
        
        Fqc fqc = fqcService.findByPrimaryKey(fqc_id);
        checkArgument(fqc != null, "Can't find fqc object in database");
        
        fqc.setRemark(remark == null || "".equals(remark.trim()) ? null : remark);
        fqcService.addAbnormalReconnectableSignAndClose(fqc, timeCost);
        return "success";
    }
    
    @RequestMapping(value = "/findProcessing", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Fqc> findProcessing(@RequestParam(name = "fqcLine.id") int fqcLine_id) {
        return fqcService.findProcessing(fqcLine_id);
    }
    
    @RequestMapping(value = "/updateRemark", method = {RequestMethod.POST})
    @ResponseBody
    protected String updateRemark(@RequestParam int id, @RequestParam(required = false) String remark) {
        Fqc fqc = fqcService.findByPrimaryKey(id);
        checkArgument(fqc != null, "Can't find fqc object in database");
        fqc.setRemark(remark);
        fqcService.update(fqc);
        return "success";
    }
}

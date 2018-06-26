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
import com.advantech.model.FqcModelStandardTime;
import com.advantech.model.FqcProductivityHistory;
import com.advantech.model.FqcTimeTemp;
import com.advantech.service.FqcModelStandardTimeService;
import com.advantech.service.FqcProductivityHistoryService;
import com.advantech.service.FqcService;
import com.advantech.service.FqcTimeTempService;
import static com.google.common.base.Preconditions.*;
import java.util.Comparator;
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
    private FqcProductivityHistoryService productivityService;

    @Autowired
    private FqcModelStandardTimeService standardTimeService;

    @Autowired
    private FqcTimeTempService timeTempService;

    //FqcLine should not be null
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    public Fqc insert(@Valid @ModelAttribute Fqc fqc) {
        fqcService.checkAndInsert(fqc);
        return fqc;
    }

    @RequestMapping(value = "/reconnectAbnormal", method = {RequestMethod.POST})
    @ResponseBody
    public FqcProductivityHistory reconnectAbnormal(@Valid @RequestBody Fqc fqc) {
        FqcProductivityHistory p = productivityService.findByFqc(fqc.getId());
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
        fqcService.stationComplete(fqc_id, remark, timeCost);
        return "success";
    }

    @RequestMapping(value = "/addAbnormalReconnectableSignAndClose", method = {RequestMethod.POST})
    @ResponseBody
    protected String addAbnormalReconnectableSignAndClose(
            @RequestParam(name = "fqc.id") int fqc_id,
            @RequestParam int timeCost,
            @RequestParam String remark
    ) {
        fqcService.addAbnormalReconnectableSignAndClose(fqc_id, remark, timeCost);
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

    @RequestMapping(value = "/findStandardTime", method = {RequestMethod.POST})
    @ResponseBody
    protected FqcModelStandardTime findStandardTime(@RequestParam String modelName) {
        List<FqcModelStandardTime> l = standardTimeService.findAll();
        FqcModelStandardTime standardTime = l.stream()
                .filter(f -> modelName.contains(f.getModelNameCategory()))
                .max(Comparator.comparing(s -> s.getModelNameCategory().length()))
                .orElseGet(FqcModelStandardTime::new);
        return standardTime;
    }

    @RequestMapping(value = "/pauseTimeAndSaveTemp", method = {RequestMethod.POST})
    @ResponseBody
    protected String pauseTimeAndSaveTemp(
            @RequestParam(name = "fqc.id") int fqc_id,
            @RequestParam int timePeriod) {
        Fqc f = fqcService.findByPrimaryKey(fqc_id);
        timeTempService.insert(new FqcTimeTemp(f, timePeriod));
        return "success";
    }

    @RequestMapping(value = "/resumeAndRemoveTemp", method = {RequestMethod.POST})
    @ResponseBody
    protected String resumeAndRemoveTemp(
            @RequestParam(name = "fqc.id") int fqc_id) {
        FqcTimeTemp temp = timeTempService.findByFqc(fqc_id);
        if (temp != null) {
            timeTempService.delete(temp);
        }
        return "success";
    }
}

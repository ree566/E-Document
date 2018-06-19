/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.model.view.BabAvg;
import com.advantech.service.BabService;
import com.advantech.service.LineBalancingService;
import com.advantech.service.SqlViewService;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping(value = "/BabController")
public class BabController {

    @Autowired
    private BabService babService;

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private LineBalancingService lineBalancingService;

    @RequestMapping(value = "/findByMultipleClause", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Bab> findByMultipleClause(
            @RequestParam int lineType_id,
            @RequestParam int sitefloor_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam boolean aboveStandard) {
        return babService.findByMultipleClause(startDate, endDate, lineType_id, sitefloor_id, aboveStandard);
    }

    @RequestMapping(value = "/findProcessingByTagName", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Bab> findProcessingByTagName(@RequestParam String tagName) {
        return babService.findProcessingByTagName(tagName);
    }

    @RequestMapping(value = "/findByModelAndDate", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findByModelAndDate(
            @RequestParam String modelName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {
        return new DataTableResponse(babService.findByModelAndDate(modelName, startDate, endDate));
    }

    @RequestMapping(value = "/findAllModelName", method = {RequestMethod.GET})
    @ResponseBody
    public List<String> findAllModelName() {
        return babService.findAllModelName();
    }

    @RequestMapping(value = "/findBabDetail", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findBabDetail(
            @RequestParam String lineTypeName,
            @RequestParam String floorName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam boolean isAboveStandard
    ) {
        List l = sqlViewService.findBabDetail(lineTypeName, floorName, startDate, endDate, isAboveStandard);
        return new DataTableResponse(l);
    }

    @RequestMapping(value = "/findLineBalanceCompareByBab", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findLineBalanceCompare(
            @RequestParam int bab_id
    ) {
        Bab b = babService.findByPrimaryKey(bab_id);
        List<Map> l = sqlViewService.findLineBalanceCompareByBab(bab_id);
        Map m = l.get(0);

        m.put("exp_avgs",
                b.getBabStatus() == BabStatus.CLOSED ? getAvg(sqlViewService.findBabAvgInHistory(bab_id))
                : b.getBabStatus() == null ? getAvg(sqlViewService.findBabAvg(bab_id)) : 0);

        Integer ctrl_bab = (Integer) m.get("ctrl_id");
        m.put("ctrl_avgs", ctrl_bab == null ? 0 : getAvg(sqlViewService.findBabAvgInHistory(ctrl_bab)));
        return new DataTableResponse(l);
    }

    private Double getAvg(List<BabAvg> l) {
        return lineBalancingService.caculateLineBalance(l);
    }

    @RequestMapping(value = "/findLineBalanceCompare", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findLineBalanceCompare(
            @RequestParam String modelName,
            @RequestParam String lineTypeName
    ) {
        List l = sqlViewService.findLineBalanceCompare(modelName, lineTypeName);
        return new DataTableResponse(l);
    }

    @RequestMapping(value = "/findSensorStatusPerStationToday", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findSensorStatusPerStationToday() {
        return new DataTableResponse(sqlViewService.findSensorStatusPerStationToday());
    }

}

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
import static com.google.common.base.Preconditions.checkArgument;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
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
@RequestMapping(value = "/SqlViewController")
public class SqlViewController {

    @Autowired
    private BabService babService;

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private LineBalancingService lineBalancingService;

    @RequestMapping(value = "/findBabDetail", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findBabDetail(
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
    protected DataTableResponse findLineBalanceCompare(
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
    protected DataTableResponse findLineBalanceCompare(
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

    @RequestMapping(value = "/findBabPcsDetail", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findBabPcsDetail(
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String lineType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {

        if (modelName == null || "".equals(modelName.trim())) {
            modelName = null;
        }

        if ("-1".equals(lineType)) {
            lineType = null;
        }

        //modelName 或 lineType 為空時一定要設定 startDate & endDate
        checkArgument(!((modelName == null || lineType == null) && (startDate == null || endDate == null)),
                "\"ModelName\"或\"類別\"為空時，一定要設定\"startDate\"&\"endDate\"");

        if (modelName == null && startDate != null && endDate != null) {
            checkArgument(Weeks.weeksBetween(startDate.toLocalDate(), endDate.toLocalDate()).getWeeks() <= 2,
                    "日期區間不得超過2週");
        }

        return new DataTableResponse(sqlViewService.findBabPcsDetail(modelName, lineType, startDate, endDate));
    }

    @RequestMapping(value = "/findBabLineProductivity", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findBabLineProductivity(
            @RequestParam(required = false) String po,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) Integer line_id,
            @RequestParam(required = false) String jobnumber,
            @RequestParam(required = false) Integer minPcs,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {
        return new DataTableResponse(sqlViewService.findBabLineProductivity(po, modelName, line_id, jobnumber, minPcs, startDate, endDate));
    }

}

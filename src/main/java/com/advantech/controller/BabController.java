/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.model.view.BabAvg;
import com.advantech.service.BabBalanceHistoryService;
import com.advantech.service.BabPcsDetailHistoryService;
import com.advantech.service.BabService;
import com.advantech.service.SqlViewService;
import com.advantech.webservice.WebServiceRV;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private BabPcsDetailHistoryService babPcsDetailHistoryService;

    @Autowired
    private BabBalanceHistoryService babBalanceHistoryService;

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

    @RequestMapping(value = "/findModelNameByPo", method = {RequestMethod.GET})
    @ResponseBody
    protected String findModelNameByPo(@RequestParam String po) {
        String modelname = WebServiceRV.getInstance().getModelnameByPo(po);
        return (modelname == null ? "data not found" : convertString(modelname));
    }

    @RequestMapping(value = "/findProcessingByLine", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Bab> findProcessingByLine(@RequestParam int line_id) {
        return babService.findProcessingByLine(line_id);
    }

    private String convertString(String input) {
        String converstring = "";
        Pattern p = Pattern.compile("[\\w|-]");
        Matcher matcher = p.matcher(input);
        while (matcher.find()) {
            converstring += matcher.group();
        }
        return converstring;
    }

    //Find history info and transform data into chart info
    @RequestMapping(value = "/getSensorDiffChart", method = {RequestMethod.GET})
    @ResponseBody
    protected String getSensorDiffChart(@ModelAttribute Bab bab) {
        BabStatus status = bab.getBabStatus();
        List l;
        if (null == status) {
            l = sqlViewService.findSensorStatus(bab.getId());
        } else {
            switch (status) {
                case CLOSED:
                    l = babPcsDetailHistoryService.findByBab(bab.getId());
                default:
                    l = new ArrayList();
            }
        }

        return this.toChartForm(l).toString();
    }

    private JSONArray toChartForm(List<Map> l) {
        JSONArray totalArrObj = new JSONArray();
        JSONObject innerObj = new JSONObject();
        JSONArray arr = new JSONArray();
        String tName = "";
        int loopCount = 1;
        int listSize = l.size();
        for (Map m : l) {
            boolean isLast = loopCount == listSize;
            String st = (String) m.get("TagName");
            int groupid = (int) m.get("groupid");
            if ("".equals(tName)) {
                tName = st;
            }
            if (!st.equals(tName) || isLast) {
                innerObj.put("name", tName);
                innerObj.put("dataPoints", arr);
                totalArrObj.put(innerObj);
                if (!isLast) {
                    innerObj = new JSONObject();
                    arr = new JSONArray();
                }
                tName = st;
            }
            arr.put(new JSONObject().put("x", groupid).put("y", m.get("diff")));
            loopCount++;
            //Bug when data group only have one
        }
        return totalArrObj;
    }

    //Find history info and transform data into chart info
    @RequestMapping(value = "/findBabTotalAvg", method = {RequestMethod.GET})
    @ResponseBody
    protected Double findBabTotalAvg(@ModelAttribute Bab bab) {
        BabStatus status = bab.getBabStatus();
        List<BabAvg> l;
        if (null == status) {
            l = sqlViewService.findBabAvg(bab.getId());
        } else switch (status) {
            case CLOSED:
                l = sqlViewService.findBabAvgInHistory(bab.getId());
                break;
            default:
                l = new ArrayList();
                break;
        }
        return this.calcTotalAvg(l);
    }

    public Double calcTotalAvg(List<BabAvg> l) {
        Double total = 0.0;
        int people = l.size();
        total = l.stream().map((b) -> b.getAverage()).reduce(total, (accumulator, _item) -> accumulator + _item);
        return total / people;
    }

    @RequestMapping(value = "/findByModelAndDate", method = {RequestMethod.GET})
    @ResponseBody
    public List<Bab> findByModelAndDate(
            @RequestParam String modelName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {
        return babService.findByModelAndDate(modelName, startDate, endDate);
    }

    @RequestMapping(value = "/findAllModelName", method = {RequestMethod.GET})
    @ResponseBody
    public List<String> findAllModelName() {
        return babService.findAllModelName();
    }

    @RequestMapping(value = "/findLineBalanceDetail", method = {RequestMethod.GET})
    @ResponseBody
    public List findLineBalanceDetail(@ModelAttribute Bab bab) {
        if (null == bab.getBabStatus()) { //Processing
            return sqlViewService.findBalanceDetail(bab.getId());
        } else {
            switch (bab.getBabStatus()) {
                case CLOSED:
                    return babPcsDetailHistoryService.findByBab(bab.getId());
                default:
                    return new ArrayList();
            }
        }
    }

}

package com.advantech.controller;

import com.advantech.converter.BabStatusControllerConverter;
import com.advantech.datatable.DataTableResponse;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import com.advantech.service.BabBalanceHistoryService;
import com.advantech.service.BabPcsDetailHistoryService;
import com.advantech.service.BabService;
import com.advantech.service.SqlViewService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabChartController")
public class BabChartController {

    @Autowired
    private BabService babService;

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private BabPcsDetailHistoryService babPcsDetailHistoryService;

    @Autowired
    private BabBalanceHistoryService babBalanceHistoryService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(BabStatus.class, new BabStatusControllerConverter());
    }

    @RequestMapping(value = "/findLineBalanceDetail", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse findLineBalanceDetail(
            @ModelAttribute Bab b
    ) {
        List l;
        if (b == null) {
            return new DataTableResponse(new ArrayList());
        }
        if (null == b.getBabStatus()) { //Still proccessing
            l = sqlViewService.findBalanceDetail(b.getId());
        } else {
            switch (b.getBabStatus()) {
                case CLOSED:
                    l = babBalanceHistoryService.findByBab(b.getId());
                    break;
                default:
                    l = new ArrayList();
                    break;
            }
        }
        return new DataTableResponse(l);
    }

    //Find history info and transform data into chart info
    @RequestMapping(value = "/getSensorDiffChart", method = {RequestMethod.GET})
    @ResponseBody
    protected Map getSensorDiffChart(@ModelAttribute Bab bab) {
        BabStatus status = bab.getBabStatus();
        List<Map> l;
        if (null == status) {
            l = sqlViewService.findSensorStatus(bab.getId());
        } else {
            switch (status) {
                case CLOSED:
                    l = babPcsDetailHistoryService.findByBabForMap(bab.getId());
                    break;
                default:
                    l = new ArrayList();
                    break;
            }
        }

        return this.toChartForm(l);
    }

    private Map toChartForm(List<Map> l) {
        List<Map<String, Object>> total = new ArrayList();
        int diffSum = 0;
        int maxGroup = 0;
        for (Map m : l) {
            String tagName = (String) m.get("tagName");
            Integer groupid = toInt(m.get("groupid").toString());
            Integer diff = toInt(m.get("diff").toString());
            Map filter = total.stream()
                    .filter(i -> i.containsKey("name") && i.get("name").equals(tagName))
                    .findFirst().orElse(null);
            if (filter == null) {
                Map tagInfo = new HashMap();
                tagInfo.put("name", tagName);
                List dataPoints = new ArrayList();
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", diff);
                tagInfo.put("dataPoints", dataPoints);
                total.add(tagInfo);
            } else {
                List dataPoints = (List) filter.get("dataPoints");
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", m.get("diff"));
                dataPoints.add(dataPoint);
            }
            diffSum += diff;
            if (maxGroup < groupid) {
                maxGroup = groupid;
            }
        }

        Map infoWithAvg = new HashMap();
        if (!total.isEmpty()) {
            int people = total.size();
            infoWithAvg.put("avg", (diffSum / people / maxGroup));
        }
        infoWithAvg.put("data", total);

        return infoWithAvg;
    }
}

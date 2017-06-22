/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.jqgrid.JqGridResponse;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeService;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/Audit")
@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_GUEST"})
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private WorktimeService worktimeService;

    @ResponseBody
    @RequestMapping(value = "/find/{modelName}/{version}", method = {RequestMethod.GET, RequestMethod.POST})
    public JqGridResponse getAudit(
            @PathVariable(value = "modelName") final String modelName,
            @PathVariable(value = "version") final int version,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @ModelAttribute PageInfo info) {

        List l = new ArrayList();
        if (startDate != null && endDate != null) {
            DateTime d1 = startDate.withTimeAtStartOfDay();
            DateTime d2 = endDate.withHourOfDay(23).withMinuteOfHour(59);

            if ("-1".equals(modelName) && version == -1) {
                l = auditService.findByDate(Worktime.class, info, d1.toDate(), d2.toDate());
            } else if (!"-1".equals(modelName)) {
                Worktime w = worktimeService.findByModel(modelName);
                if (w == null) {
                    l = new ArrayList();
                } else {
                    l = auditService.findByDate(Worktime.class, w.getId(), info, d1.toDate(), d2.toDate());
                }
            }
        }

        JqGridResponse resp = toJqGridResponse(l, info);
        return resp;
    }

//    2017-06-06 11:26:38 AM
    @ResponseBody
    @RequestMapping(value = "/findLastRevision", method = {RequestMethod.GET, RequestMethod.POST})
    public Number getAuditRevision(@RequestParam(required = false) Integer id) {
        return auditService.findLastRevisions(Worktime.class, id);
    }

}

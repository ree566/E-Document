/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.response.JqGridResponse;
import com.advantech.service.AuditService;
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
@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_GUEST"})
public class AuditController {

    @Autowired
    private AuditService auditService;

    @ResponseBody
    @RequestMapping(value = "/getAudit.do/{id}/{version}", method = {RequestMethod.GET, RequestMethod.POST})
    public JqGridResponse getAudit(
            @PathVariable(value = "id") final int id,
            @PathVariable(value = "version") final int version,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @ModelAttribute PageInfo info) {

        List l;
        if (id == -1 && version == -1 && startDate != null && endDate != null) {
            DateTime d1 = startDate.withTimeAtStartOfDay();
            DateTime d2 = endDate.withHourOfDay(23).withMinuteOfHour(59);

            l = auditService.findByDate(Worktime.class, info, d1.toDate(), d2.toDate());
        } else if (id != -1) {
            l = auditService.findByPrimaryKey(Worktime.class, id);
        } else {
            l = new ArrayList();
            l.add(auditService.findByPrimaryKeyAndVersion(Worktime.class, id, version));
        }

        JqGridResponse resp = toJqGridResponse(l, info);
        return resp;
    }

}

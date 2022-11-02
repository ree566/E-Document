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
import com.advantech.model.View;
import com.advantech.service.WorktimeAuditService;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
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
@RequestMapping("/Audit")
public class AuditController {

    @Autowired
    private WorktimeAuditService worktimeAuditService;

    @Autowired
    private WorktimeService worktimeService;

    @ResponseBody
    @RequestMapping(value = "/find", method = {RequestMethod.GET, RequestMethod.POST})
    @JsonView(View.Public.class)
    protected JqGridResponse getAudit(
            @RequestParam(required = false) final Integer id,
            @RequestParam(required = false) final String modelName,
            @RequestParam(required = false) final Integer version,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @ModelAttribute PageInfo info) throws Exception {

        List l = this.findRevision(id, modelName, version, startDate, endDate, info);
        JqGridResponse resp = toJqGridResponse(l, info);
        return resp;
    }

    private List findRevision(Integer id, String modelName, Integer version, DateTime startDate, DateTime endDate, PageInfo info) throws Exception {
        List l = new ArrayList();
        if (startDate != null && endDate != null) {
            DateTime d1 = startDate.withTimeAtStartOfDay();
            DateTime d2 = endDate.withHourOfDay(23).withMinuteOfHour(59);

            if (id == null && "".equals(modelName) && version == null) {
                l = worktimeAuditService.findByDate(info, d1.toDate(), d2.toDate());
            } else if (!"".equals(modelName)) {
                Worktime w = worktimeService.findByModel(modelName);
                if (w == null) {
//                Search the revision when model not found.
//                If revision still no info, model is not exist.
                    PageInfo tempInfo = addModelNameFilterAndGetClone(info, modelName);
                    List<Object[]> auditInfo = worktimeAuditService.findAll(tempInfo);
                    Worktime auditW = (auditInfo.isEmpty() ? null : (Worktime) auditInfo.get(0)[0]);
                    w = auditW != null ? auditW : null;
                }
                if (w != null) {
                    l = worktimeAuditService.findByDate(w.getId(), info, d1.toDate(), d2.toDate());
                } else {
                    PageInfo tempInfo = addModelNameFilterAndGetClone(info, modelName);
                    tempInfo.setSearchOper("bw");
                    l = worktimeAuditService.findByDate(tempInfo, d1.toDate(), d2.toDate());
                }
            } else if (id != null) {
                l = worktimeAuditService.findByDate(id, info, d1.toDate(), d2.toDate());
            }
        } else if (startDate == null && endDate == null) {
            if (id != null) {
                l = worktimeAuditService.findByPrimaryKey(id);
            } else if (modelName != null && !"".equals(modelName)) {
                PageInfo tempInfo = addModelNameFilterAndGetClone(info, modelName);
                l = worktimeAuditService.findAll(tempInfo);
        }
        }
        return l;
    }

    private PageInfo addModelNameFilterAndGetClone(PageInfo p, String modelName) throws CloneNotSupportedException {
        PageInfo tempInfo = p.clone();
        tempInfo.set_Search(true);
        tempInfo.setSearchField("modelName");
        tempInfo.setSearchOper("eq");
        tempInfo.setSearchString(modelName);
        tempInfo.setMaxNumOfRows(1);
        return tempInfo;
    }

    @ResponseBody
    @RequestMapping(value = "/findLastRevision", method = {RequestMethod.GET, RequestMethod.POST})
    protected Number getAuditRevision(@RequestParam(required = false) Integer id) {
        return worktimeAuditService.findLastRevisions(id);
    }

}

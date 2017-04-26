/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.Identit;
import com.advantech.model.Worktime;
import com.advantech.response.JqGridResponse;
import com.advantech.service.AuditService;
import com.advantech.service.IdentitService;
import com.advantech.service.WorktimeService;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@SessionAttributes({"user"})
public class TestRequestController {

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private IdentitService identitService;

    @Autowired
    private AuditService auditService;

    @ResponseBody
    @RequestMapping(value = "/getSomeTable.do/{id}/{version}", method = {RequestMethod.GET, RequestMethod.POST})
    public JqGridResponse getSheetView(
            @PathVariable(value = "id") final int id,
            @PathVariable(value = "version") final int version,
            @ModelAttribute PageInfo info,
            @ModelAttribute("user") Identit user) {

        List l;
        if (id == -1 && version == -1) {
            l = auditService.findAll(Worktime.class);
        } else if (version == -1) {
            l = auditService.findByPrimaryKey(Worktime.class, id);
        } else if (id != -1 && version == 999) {
            l = auditService.findReversions(Worktime.class, id);
        } else if (id == 999 && version != -1) {
            l = auditService.forEntityAtReversion(Worktime.class, version);
        } else {
            l = new ArrayList();
            l.add(auditService.findByPrimaryKeyAndVersion(Worktime.class, id, version));
        }

        return getResponseObject(l, info);
    }

    private JqGridResponse getResponseObject(List l, PageInfo info) {
        JqGridResponse viewResp = new JqGridResponse();
        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;
        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());
        return viewResp;
    }

    //The request param name indexOf('name') == 1 is selected box which has one to many relationship.
    @RequestMapping(value = "/testRequest.do", method = {RequestMethod.POST})
    public ResponseEntity testRequest(
            @ModelAttribute("user") Identit user,
            @ModelAttribute Flow flow,
            HttpServletRequest req) throws ServletException, IOException {

        this.showParams(req);

        System.out.println(new Gson().toJson(flow));

        return ResponseEntity.status(HttpStatus.CREATED).body("testing");
    }

    private void showParams(HttpServletRequest req) throws ServletException, IOException {
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/testUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity testUpdate(
            @ModelAttribute("user") Identit user) {

        List<Worktime> l = worktimeService.findByPrimaryKeys(5932, 5933, 5937);

        for (Worktime w : l) {
            int i = (int) Math.round(Math.random() * 10) + 1;
            String modelName = w.getModelName();
            w.setModelName(modelName.substring(0, modelName.length() - 1) + i);
        }

        int flag = worktimeService.update(l);

        return ResponseEntity
                .status(flag == 1 ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(flag == 1 ? "SUCCESS" : "FAIL");
    }
}

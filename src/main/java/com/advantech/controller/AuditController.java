/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import com.advantech.model.Worktime;
import com.advantech.response.JqGridResponse;
import com.advantech.service.AuditService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuditController {

    @Autowired
    private AuditService auditService;

    @ResponseBody
    @RequestMapping(value = "/getAudit.do/{id}/{version}", method = {RequestMethod.GET, RequestMethod.POST})
    public JqGridResponse getAudit(
            @PathVariable(value = "id") final int id,
            @PathVariable(value = "version") final int version,
            @ModelAttribute PageInfo info,
            @ModelAttribute("user") Identit user) {

        List l;
        if (id == -1 && version == -1) {
            l = auditService.findAll(Worktime.class, info);
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
        
        JqGridResponse resp = toJqGridResponse(l, info);
        return resp;
    }

}

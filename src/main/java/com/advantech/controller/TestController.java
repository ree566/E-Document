/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeService;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng For controller test, can be removed.
 */
@Controller
@Secured({"ROLE_ADMIN", "ROLE_GUEST"})
@RequestMapping(value = "/testCtrl")
public class TestController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private WorktimeService worktimeService;

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public String test() {
        return "hi";
    }

    @ResponseBody
    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    @Transactional
    protected String test1() throws Exception {
        PageInfo info = new PageInfo();
        info.setRows(Integer.MAX_VALUE);
        info.setSearchField("id");
        info.setSearchOper("lt");
        info.setSearchString("8527");
        List<Worktime> l = worktimeService.findAll(info);
        for (Worktime w : l) {
            w.setTotalModule(w.getTotalModule().subtract(w.getCleanPanel()));
            w.setAssy(w.getAssy().add(w.getCleanPanel()));
            worktimeService.update(w);
//            port.uploadStandardTime(w);
        }
        return "hi1";
    }

    @ResponseBody
    @RequestMapping(value = "/test2/{id}", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    protected List test2(@PathVariable int id) {
        return auditService.findByPrimaryKey(Worktime.class, id);
    }

    @ResponseBody
    @RequestMapping(value = "/test3/{sD}/{eD}", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    protected void test3(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime sD,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime eD) {
        System.out.println(sD);
        System.out.println(eD);
    }

}

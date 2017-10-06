/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured("ROLE_ADMIN")
    public String test(HttpServletResponse response) throws Exception {
        return "test";
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

    @ResponseBody
    @RequestMapping(value = "/exceptionTest", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured("ROLE_ADMIN")
    protected void exceptionTest(HttpServletResponse resp) throws Exception {
        throw new Exception("This is a testing exception");
    }

    @ResponseBody
    @RequestMapping(value = "/runtimeExceptionTest", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured("ROLE_ADMIN")
    protected void runtimeExceptionTest(HttpServletResponse resp) throws Exception {
        throw new RuntimeException("This is a testing runtimeException");
    }

    @ResponseBody
    @RequestMapping(value = "/mavExceptionTest", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured("ROLE_ADMIN")
    protected ModelAndView mavExceptionTest() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", new Exception("This is a testing mavException"));
        mav.setViewName("pages/error");
        return mav;
    }

}

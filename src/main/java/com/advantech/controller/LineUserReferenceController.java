/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.db1.Line;
import com.advantech.model.db1.LineUserReference;
import com.advantech.model.db1.User;
import com.advantech.service.db1.LineService;
import com.advantech.service.db1.LineUserReferenceService;
import com.advantech.service.db1.UserService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/LineUserReferenceController")
public class LineUserReferenceController {

    @Autowired
    private LineUserReferenceService service;

    @Autowired
    private LineService lineService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findByLineAndDate", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findByLineAndDate(
            @ModelAttribute Line line,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime onboardDate) {

        Line lineInDb = lineService.findByPrimaryKey(line.getId());
        List<LineUserReference> l = service.findByLineAndDate(lineInDb, onboardDate);
        return new DataTableResponse(l);

    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    protected String insert(@ModelAttribute LineUserReference lf) {
        service.insert(lf);
        return "success";
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    protected String update(@ModelAttribute LineUserReference lf) {
        service.updateStationPeople(lf);
        return "success";
    }

    @RequestMapping(value = "/findUserByRole", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findUserByRole(
            @RequestParam(name = "userRole[]") String[] userRole,
            HttpServletRequest request
    ) {
        for (int i = 0; i < userRole.length; i++) {
            userRole[i] += "_USER";
        }

        List<User> l = userService.findByRole(userRole);
        l = l.stream().sorted(Comparator.comparing(User::getUsernameCh))
                .collect(Collectors.toList());
        return new DataTableResponse(l);

    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@ModelAttribute LineUserReference lf) {
        service.delete(lf);
        return "success";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.Line;
import com.advantech.model.LineUserReference;
import com.advantech.model.User;
import com.advantech.service.LineService;
import com.advantech.service.LineUserReferenceService;
import com.advantech.service.UserService;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/findByLine", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findByLine(@ModelAttribute Line line) {
        Line lineInDb = lineService.findByPrimaryKey(line.getId());
        List<LineUserReference> l = service.findByLine(lineInDb);
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
    protected DataTableResponse findUserByRole(@RequestParam final String userRole, HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_OPER_IE")) {
            List<User> l = userService.findAll();
            l = l.stream().sorted(comparing(User::getUsername)).collect(toList());
            return new DataTableResponse(l);
        } else {
            List l = new ArrayList();
            User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
            switch (userRole) {
                case "ASSY":
                    l = userService.findByFloorAndRole(user.getFloor(), "ASSY_USER");
                    break;
                case "PACKING":
                    l = userService.findByFloorAndRole(user.getFloor(), "PACKING_USER");
                    break;
                default:
                    break;
            }
            return new DataTableResponse(l);
        }

    }
    
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@ModelAttribute LineUserReference lf) {
        service.delete(lf);
        return "success";
    }

}

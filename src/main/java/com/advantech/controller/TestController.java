/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.User;
import com.advantech.response.JqGridResponse;
import com.advantech.service.UnitService;
import com.advantech.service.UserProfileService;
import com.advantech.service.UserService;
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
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private UserProfileService userProfileService;

    @ResponseBody
    @RequestMapping(value = "/test.do/{jobnumber}", method = {RequestMethod.GET, RequestMethod.POST})
    public JqGridResponse test(
            @PathVariable String jobnumber,
            @ModelAttribute PageInfo info,
            @ModelAttribute("user") User user) {
        List l = new ArrayList();
        l.add(userService.findByJobnumber(jobnumber));
        JqGridResponse viewResp = toJqGridResponse(l, info);
        return viewResp;
    }
}

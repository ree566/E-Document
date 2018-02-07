/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 組包裝異常回覆的CRUD
 */
package com.advantech.controller;

import com.advantech.model.ActionCode;
import com.advantech.model.Bab;
import com.advantech.model.Countermeasure;
import com.advantech.model.ErrorCode;
import com.advantech.model.User;
import com.advantech.service.ActionCodeService;
import com.advantech.service.BabService;
import com.advantech.service.CountermeasureService;
import com.advantech.service.ErrorCodeService;
import static com.google.common.collect.Sets.newHashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.google.common.base.Preconditions.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/CountermeasureController")
public class CountermeasureController {

    @Autowired
    private BabService babService;

    @Autowired
    private CountermeasureService cService;

    @Autowired
    private ErrorCodeService errorCodeService;

    @Autowired
    private ActionCodeService actionCodeService;

    @RequestMapping(value = "/findByBab", method = {RequestMethod.GET})
    @ResponseBody
    protected Countermeasure findByBab(@RequestParam(value = "bab_id") int bab_id) {
        return cService.findByBab(bab_id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    protected boolean update(
            @RequestParam(value = "bab_id") int bab_id,
            @RequestParam(value = "errorCodes[]") Integer[] errorCodes,
            @RequestParam(value = "actionCodes[]") Integer[] actionCodes,
            @RequestParam String solution,
            @RequestParam String sop,
            @RequestParam String editor
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        checkState(!(auth instanceof AnonymousAuthenticationToken), "查無登入紀錄，請重新登入");
        User user = (User) auth.getPrincipal();

        Countermeasure c = cService.findByBab(bab_id);
        if (c == null) {
            Bab b = babService.findByPrimaryKey(bab_id);
            c = new Countermeasure();
            c.setBab(b);
        }
        List<ErrorCode> errorCode = errorCodeService.findByPrimaryKeys(errorCodes);
        List<ActionCode> actionCode = actionCodeService.findByPrimaryKeys(actionCodes);
        c.setErrorCodes(newHashSet(errorCode));
        c.setActionCodes(newHashSet(actionCode));
        c.setLastEditor(user);
        c.setSolution(solution);
        if (c.getId() == 0) {
            cService.insert(c, sop);
        } else {
            cService.update(c, sop);
        }
        return true;
    }

    @RequestMapping(value = "/getErrorCodeOptions", method = {RequestMethod.GET})
    @ResponseBody
    protected List getErrorCode() {
        return errorCodeService.findAll();
    }

    @RequestMapping(value = "/getActionCodeOptions", method = {RequestMethod.GET})
    @ResponseBody
    protected List getActionCode() {
        return actionCodeService.findAll();
    }

}

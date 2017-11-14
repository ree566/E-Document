/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 組包裝異常回覆的CRUD
 */
package com.advantech.controller;

import com.advantech.model.ActionCode;
import com.advantech.model.Countermeasure;
import com.advantech.model.ErrorCode;
import com.advantech.service.ActionCodeService;
import com.advantech.service.CountermeasureService;
import com.advantech.service.ErrorCodeService;
import java.util.List;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/CountermeasureController")
public class CountermeasureController {

    @Autowired
    private CountermeasureService cService;

    @Autowired
    private ErrorCodeService errorCodeService;

    @Autowired
    private ActionCodeService actionCodeService;

    @RequestMapping(value = "/findByBab", method = {RequestMethod.GET})
    @ResponseBody
    protected Countermeasure findByBab(@RequestParam(value = "BABid") int bab_id) {
        return cService.findByBab(bab_id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    protected boolean update(
            @RequestParam(value = "BABid") int bab_id,
            @RequestParam(value = "errorCodes[]") Integer[] errorCodes,
            @RequestParam(value = "actionCodes[]") Integer[] actionCodes,
            @RequestParam String solution,
            @RequestParam String editor
    ) {
        Countermeasure c = cService.findByBab(bab_id);
        boolean isCreate = (c == null);
        if (isCreate) {
            c = new Countermeasure();
            c.setBABid(bab_id);
        }
        List<ErrorCode> errorCode = errorCodeService.findByPrimaryKeys(errorCodes);
        List<ActionCode> actionCode = actionCodeService.findByPrimaryKeys(actionCodes);
        c.setErrorCodes(newHashSet(errorCode));
        c.setActionCodes(newHashSet(actionCode));
        c.setLastEditor(editor);
        c.setSolution(solution);
        if (isCreate) {
            cService.insert(c);
        } else {
            cService.update(c);
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

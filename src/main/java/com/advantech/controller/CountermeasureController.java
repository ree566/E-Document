/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 組包裝異常回覆的CRUD
 */
package com.advantech.controller;

import static com.advantech.helper.SecurityPropertiesUtils.retrieveAndCheckUserInSession;
import com.advantech.model.ActionCode;
import com.advantech.model.Bab;
import com.advantech.model.Countermeasure;
import com.advantech.model.CountermeasureType;
import com.advantech.model.ErrorCode;
import com.advantech.model.User;
import com.advantech.service.ActionCodeService;
import com.advantech.service.BabService;
import com.advantech.service.CountermeasureService;
import com.advantech.service.CountermeasureTypeService;
import com.advantech.service.ErrorCodeService;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;
import java.util.List;
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
    private BabService babService;

    @Autowired
    private CountermeasureService cService;

    @Autowired
    private CountermeasureTypeService cTypeService;

    @Autowired
    private ErrorCodeService errorCodeService;

    @Autowired
    private ActionCodeService actionCodeService;

    @RequestMapping(value = "/findByBab", method = {RequestMethod.GET})
    @ResponseBody
    protected Countermeasure findByBab(
            @RequestParam(value = "bab_id") int bab_id,
            @RequestParam String typeName
    ) {
        return cService.findByBabAndTypeName(bab_id, typeName);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    protected boolean update(
            @RequestParam(value = "bab_id") int bab_id,
            @RequestParam String typeName,
            @RequestParam(value = "errorCodes[]", required = false) Integer[] errorCodes,
            @RequestParam(value = "actionCodes[]", required = false) Integer[] actionCodes,
            @RequestParam String solution,
            @RequestParam String sop,
            @RequestParam String editor
    ) {

        User user = retrieveAndCheckUserInSession();

        Countermeasure c = cService.findByBabAndTypeName(bab_id, typeName);
        CountermeasureType cType = cTypeService.findByName(typeName);
        checkArgument(cType != null);
        if (c == null) {
            Bab b = babService.findByPrimaryKey(bab_id);
            c = new Countermeasure();
            c.setBab(b);
            c.setCountermeasureType(cType);
        }
        if (errorCodes != null && errorCodes.length != 0) {
            List<ErrorCode> errorCode = errorCodeService.findByPrimaryKeys(errorCodes);
            c.setErrorCodes(newHashSet(errorCode));
        }
        if (actionCodes != null && actionCodes.length != 0) {
            List<ActionCode> actionCode = actionCodeService.findByPrimaryKeys(actionCodes);
            c.setActionCodes(newHashSet(actionCode));
        }
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

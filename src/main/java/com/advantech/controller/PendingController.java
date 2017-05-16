/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Pending;
import com.advantech.response.JqGridResponse;
import com.advantech.service.PendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/Pending")
public class PendingController extends CrudController<Pending> {

    @Autowired
    private PendingService pendingService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse findAll(PageInfo info) {
        return toJqGridResponse(pendingService.findAll(info), info);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(String oper, Pending pending, BindingResult bindingResult) {

        String modifyMessage;
        int responseFlag = 0;

        switch (oper) {
            case ADD:
                responseFlag = pendingService.insert(pending);
                break;
            case EDIT:
                responseFlag = pendingService.update(pending);
                break;
            case DELETE:
                responseFlag = pendingService.delete(pendingService.findByPrimaryKey(pending.getId()));
                break;
        }
        modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

}

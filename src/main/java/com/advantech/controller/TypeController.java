/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Type;
import com.advantech.response.JqGridResponse;
import com.advantech.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/Type")
public class TypeController extends CrudController<Type> {

    @Autowired
    private TypeService typeService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse findAll(PageInfo info) {
        return toJqGridResponse(typeService.findAll(info), info);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(String oper, Type type) {
        String modifyMessage;
        int responseFlag = 0;
        switch (oper) {
            case ADD:
                responseFlag = typeService.insert(type);
                break;
            case EDIT:
                responseFlag = typeService.update(type);
                break;
            case DELETE:
                responseFlag = typeService.delete(typeService.findByPrimaryKey(type.getId()));
                break;
        }
        modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

}

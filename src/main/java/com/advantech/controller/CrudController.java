/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.response.JqGridResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wei.Cheng
 * @param <T>
 */
public abstract class CrudController<T> {

    protected final String ADD = "add", EDIT = "edit", DELETE = "del";
    protected final String SUCCESS_MESSAGE = "SUCCESS", FAIL_MESSAGE = "FAIL";

    protected final String SELECT_URL = "/find", UPDATE_URL = "/mod";

    protected abstract JqGridResponse findAll(@ModelAttribute PageInfo info);

    protected abstract ResponseEntity update(
            @RequestParam
            final String oper, @ModelAttribute T pojo,
            BindingResult bindingResult);
}

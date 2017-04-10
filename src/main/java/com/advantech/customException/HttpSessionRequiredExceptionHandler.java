/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Wei.Cheng
 */
public class HttpSessionRequiredExceptionHandler {

    @ExceptionHandler(HttpSessionRequiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The session has expired")
    public String handleSessionExpired() {
        return "sessionExpired";
    }
}

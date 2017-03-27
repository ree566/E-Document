/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Wei.Cheng
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)  // 404
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(){
        
    }
    
    public OrderNotFoundException(String message) {
        super("Exception cause by: " + message);
    }
}

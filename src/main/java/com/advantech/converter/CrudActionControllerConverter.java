/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.controller.CrudAction;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author Wei.Cheng
 */
public class CrudActionControllerConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String capitalized = text.toUpperCase();
        CrudAction action = CrudAction.valueOf(capitalized);
        setValue(action);
    }
}

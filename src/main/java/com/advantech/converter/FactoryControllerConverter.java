/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.webservice.Factory;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author Wei.Cheng
 */
public class FactoryControllerConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Factory f = Factory.valueOf(text);
        setValue(f);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.db1.BabStatus;
import java.beans.PropertyEditorSupport;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author Wei.Cheng
 */
public class BabStatusControllerConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Integer capitalized = NumberUtils.toInt(text);
        BabStatus status = BabStatus.valueOf(capitalized);
        setValue(status);
    }
}

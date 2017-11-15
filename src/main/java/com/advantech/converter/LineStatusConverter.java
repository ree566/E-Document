/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.LineStatus;
import javax.persistence.AttributeConverter;

/**
 *
 * @author Wei.Cheng
 */
public class LineStatusConverter implements AttributeConverter<LineStatus, Integer>{

    @Override
    public Integer convertToDatabaseColumn(LineStatus x) {
        switch (x) {
            case OPEN:
                return LineStatus.OPEN.getState();
            case CLOSE:
                return LineStatus.CLOSE.getState();
            default:
                throw new IllegalArgumentException("Unknown lineStatus " + x);
        }
    }

    @Override
    public LineStatus convertToEntityAttribute(Integer y) {
        if (null == y){
            throw new IllegalArgumentException("Unknown lineStatus " + y);
        } else switch (y) {
            case 0:
                return LineStatus.CLOSE;
            case 1:
                return LineStatus.OPEN;
            default:
                throw new IllegalArgumentException("Unknown lineStatus " + y);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.db1.LineStatus;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class LineStatusConverter extends AbstractEnumConverter<LineStatus, Integer>{

   @Override
    public Integer convertToDatabaseColumn(LineStatus x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public LineStatus convertToEntityAttribute(Integer y) {
        return this.toEntityAttribute(LineStatus.class, y);
    }
    
}

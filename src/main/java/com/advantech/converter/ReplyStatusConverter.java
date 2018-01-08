/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.ReplyStatus;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class ReplyStatusConverter extends AbstractEnumConverter<ReplyStatus>{

    @Override
    public Integer convertToDatabaseColumn(ReplyStatus x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public ReplyStatus convertToEntityAttribute(Integer y) {
        return this.toEntityAttribute(ReplyStatus.class, y);
    }
    
}

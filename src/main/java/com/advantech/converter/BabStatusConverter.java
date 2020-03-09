/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.db1.BabStatus;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class BabStatusConverter extends AbstractEnumConverter<BabStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BabStatus x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public BabStatus convertToEntityAttribute(Integer y) {
        return this.toEntityAttribute(BabStatus.class, y);
    }

}

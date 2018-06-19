/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.webservice.Factory;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class FactoryConverter extends AbstractEnumConverter<Factory, String>{

   @Override
    public String convertToDatabaseColumn(Factory x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public Factory convertToEntityAttribute(String y) {
        return this.toEntityAttribute(Factory.class, y);
    }
    
}

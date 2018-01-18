/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.security.State;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class UserStatusConverter extends AbstractEnumConverter<State, String> {

    @Override
    public String convertToDatabaseColumn(State x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public State convertToEntityAttribute(String y) {
        return this.toEntityAttribute(State.class, y);
    }

}

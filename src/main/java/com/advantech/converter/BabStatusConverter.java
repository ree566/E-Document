/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.BabStatus;
import java.util.Objects;
import javax.persistence.AttributeConverter;

/**
 *
 * @author Wei.Cheng
 */
public class BabStatusConverter implements AttributeConverter<BabStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BabStatus x) {
        return x == null ? null : x.getValue();
    }

    @Override
    public BabStatus convertToEntityAttribute(Integer y) {
        if (y == null) {
            return null;
        }
        for (BabStatus e : BabStatus.values()) {
            if (Objects.equals(e.getValue(), y)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown babStatus " + y);
    }

}

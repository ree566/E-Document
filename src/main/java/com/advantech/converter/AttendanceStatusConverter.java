/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.AttendanceStatus;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class AttendanceStatusConverter extends AbstractEnumConverter<AttendanceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AttendanceStatus x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public AttendanceStatus convertToEntityAttribute(Integer y) {
        return this.toEntityAttribute(AttendanceStatus.class, y);
    }

}

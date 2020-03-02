/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.db1.BabDataCollectMode;
import javax.persistence.Converter;

/**
 *
 * @author Wei.Cheng
 */
@Converter(autoApply = true)
public class BabCollectModeConverter extends AbstractEnumConverter<BabDataCollectMode, String> {

    @Override
    public String convertToDatabaseColumn(BabDataCollectMode x) {
        return this.toDatabaseColumn(x);
    }

    @Override
    public BabDataCollectMode convertToEntityAttribute(String y) {
        return this.toEntityAttribute(BabDataCollectMode.class, y);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import javax.persistence.AttributeConverter;

/**
 *
 * @author Wei.Cheng
 * @param <E>
 */
public abstract class AbstractEnumConverter<E extends Enum<E> & Encodeable> implements AttributeConverter<E, Integer> {

    public Integer toDatabaseColumn(E attr) {
        return (attr == null)
                ? null
                : attr.token();
    }

    public E toEntityAttribute(Class<E> cls, Integer dbCol) {
        return (dbCol == null)
                ? null
                : Encodeable.forToken(cls, dbCol);
    }
}

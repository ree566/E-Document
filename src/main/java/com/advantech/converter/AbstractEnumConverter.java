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
 * @param <K>
 */
public abstract class AbstractEnumConverter<E extends Enum<E> & Encodeable, K extends Object> implements AttributeConverter<E, K> {

    public K toDatabaseColumn(E attr) {
        return (attr == null)
                ? null
                : (K) attr.token();
    }

    public E toEntityAttribute(Class<E> cls, K dbCol) {
        return (dbCol == null)
                ? null
                : Encodeable.forToken(cls, dbCol);
    }
}

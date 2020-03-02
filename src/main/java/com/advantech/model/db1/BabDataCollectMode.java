/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.advantech.converter.Encodeable;

/**
 *
 * @author Wei.Cheng
 */
public enum BabDataCollectMode implements Encodeable {
    MANUAL,
    AUTO;

    @Override
    public Object token() {
        return this.toString();
    }
}

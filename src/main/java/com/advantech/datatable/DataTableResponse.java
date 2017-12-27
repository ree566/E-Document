/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.datatable;

/**
 *
 * @author Wei.Cheng
 */
public class DataTableResponse {

    private Object data;

    public DataTableResponse() {
    }

    public DataTableResponse(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}

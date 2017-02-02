/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.io.Serializable;

/**
 *
 * @author Wei.Cheng
 * 工單帶機種功能已經轉為向WebService下request, 後續可刪除此class
 */
//@Entity
public class PrepareSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    private String po;
    private String Model_name;

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getModel_name() {
        return Model_name;
    }

    public void setModel_name(String Model_name) {
        this.Model_name = Model_name;
    }

}

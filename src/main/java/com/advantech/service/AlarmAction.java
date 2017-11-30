/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public interface AlarmAction {

    public void initAlarmSign();

    public void setAlarmSign(List<com.advantech.model.AlarmAction> l);

    public void resetAlarmSign();

    public void setAlarmSignToTestingMode();
}

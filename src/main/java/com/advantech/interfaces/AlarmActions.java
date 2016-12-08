/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.interfaces;

import com.advantech.entity.AlarmAction;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public interface AlarmActions {
    public boolean insertAlarm(List<AlarmAction> l);

    public boolean updateAlarm(List<AlarmAction> l);

    public boolean resetAlarm();

    public boolean removeAlarmSign();

}

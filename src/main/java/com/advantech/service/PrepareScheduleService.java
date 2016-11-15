/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.PrepareScheduleDAO;
import com.advantech.entity.PrepareSchedule;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class PrepareScheduleService {

    private final PrepareScheduleDAO prepareScheduleDAO;

    protected PrepareScheduleService() {
        prepareScheduleDAO = new PrepareScheduleDAO();
    }

    public PrepareSchedule getPrepareSchedule(String po) {
        List l = prepareScheduleDAO.getPrepareSchedule(po);
        return l != null && !l.isEmpty() ? (PrepareSchedule) l.get(0) : null;
    }
}

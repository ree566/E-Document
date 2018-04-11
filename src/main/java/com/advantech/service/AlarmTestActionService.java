/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AlarmTestActionDAO;
import com.advantech.model.AlarmTestAction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service(value = "alarmTestActionService")
@Transactional
public class AlarmTestActionService {

    @Autowired
    private AlarmTestActionDAO alarmTestActionDAO;

    public List<AlarmTestAction> findAll() {
        return alarmTestActionDAO.findAll();
    }

    public AlarmTestAction findByPrimaryKey(Object obj_id) {
        return alarmTestActionDAO.findByPrimaryKey(obj_id);
    }

    public int insert(AlarmTestAction pojo) {
        return alarmTestActionDAO.insert(pojo);
    }

    public boolean insert(List<AlarmTestAction> l) {
        alarmTestActionDAO.insert(l);
        return true;
    }

    public int update(AlarmTestAction pojo) {
        return alarmTestActionDAO.update(pojo);
    }

    public boolean update(List<AlarmTestAction> l) {
        alarmTestActionDAO.update(l);
        return true;
    }

    public boolean reset() {
        List<AlarmTestAction> l = this.findAll();
        l.forEach((a) -> {
            a.setAlarm(0);
        });
        this.update(l);
        return true;
    }

    public int delete(AlarmTestAction pojo) {
        return alarmTestActionDAO.delete(pojo);
    }

    public int delete(List<AlarmTestAction> l) {
        alarmTestActionDAO.delete(l);
        return 1;
    }

    public void AlarmToTestingMode() {
        List<AlarmTestAction> l = this.findAll();
        l.forEach((a) -> {
            a.setAlarm(1);
        });
        alarmTestActionDAO.update(l);
    }

}

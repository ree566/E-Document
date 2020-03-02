/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.AlarmBabActionDAO;
import com.advantech.model.db1.AlarmBabAction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class AlarmBabActionService {

    @Autowired
    private AlarmBabActionDAO alarmBabActionDAO;

    public List<AlarmBabAction> findAll() {
        return alarmBabActionDAO.findAll();
    }

    public AlarmBabAction findByPrimaryKey(Object obj_id) {
        return alarmBabActionDAO.findByPrimaryKey(obj_id);
    }

    public int insert(AlarmBabAction pojo) {
        return alarmBabActionDAO.insert(pojo);
    }

    public boolean insert(List<AlarmBabAction> l) {
        alarmBabActionDAO.insert(l);
        return true;
    }

    public int update(AlarmBabAction pojo) {
        return alarmBabActionDAO.update(pojo);
    }

    public boolean update(List<AlarmBabAction> l) {
        alarmBabActionDAO.update(l);
        return true;
    }

    public boolean reset() {
        List<AlarmBabAction> l = this.findAll();
        l.forEach((a) -> {
            a.setAlarm(0);
        });
        this.update(l);
        return true;
    }

    public int delete(AlarmBabAction pojo) {
        return alarmBabActionDAO.delete(pojo);
    }

    public int delete(List<AlarmBabAction> l) {
        alarmBabActionDAO.delete(l);
        return 1;
    }

    public void AlarmToTestingMode() {
        List<AlarmBabAction> l = this.findAll();
        l.forEach((a) -> {
            a.setAlarm(1);
        });
        this.update(l);
    }

}

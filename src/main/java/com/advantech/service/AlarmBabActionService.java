/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AlarmBabActionDAO;
import com.advantech.model.AlarmBabAction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
public class AlarmBabActionService implements AlarmTest {

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
        for (AlarmBabAction a : l) {
            this.insert(a);
        }
        return true;
    }

    public int update(AlarmBabAction pojo) {
        return alarmBabActionDAO.update(pojo);
    }

    public boolean update(List<AlarmBabAction> l) {
        for (AlarmBabAction a : l) {
            this.update(a);
        }
        return true;
    }

    public boolean reset() {
        List<AlarmBabAction> l = this.findAll();
        for (AlarmBabAction a : l) {
            a.setAlarm(0);
            this.update(a);
        }
        return true;
    }

    public int delete(AlarmBabAction pojo) {
        return alarmBabActionDAO.delete(pojo);
    }

    public int delete(List<AlarmBabAction> l) {
        for (AlarmBabAction a : l) {
            this.delete(a);
        }
        return 1;
    }

    @Override
    public void AlarmToTestingMode() {
        List<AlarmBabAction> l = this.findAll();
        for (AlarmBabAction a : l) {
            a.setAlarm(1);
            this.update(a);
        }
    }

}

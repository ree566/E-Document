/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AlarmBabActionDAO;
import com.advantech.model.AlarmBabAction;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        l.forEach((a) -> {
            this.insert(a);
        });
        return true;
    }

    public int update(AlarmBabAction pojo) {
        return alarmBabActionDAO.update(pojo);
    }

    public boolean update(List<AlarmBabAction> l) {
        Date d = new Date();
        l.stream().map((a) -> {
            this.update(a);
            return a;
        }).forEachOrdered((a) -> {
            a.setLastUpdateTime(d);
        });
        return true;
    }

    public boolean reset() {
        List<AlarmBabAction> l = this.findAll();
        l.stream().map((a) -> {
            a.setAlarm(0);
            return a;
        }).forEachOrdered((a) -> {
            this.update(a);
        });
        return true;
    }

    public int delete(AlarmBabAction pojo) {
        return alarmBabActionDAO.delete(pojo);
    }

    public int delete(List<AlarmBabAction> l) {
        l.forEach((a) -> {
            this.delete(a);
        });
        return 1;
    }

    public void AlarmToTestingMode() {
        List<AlarmBabAction> l = this.findAll();
        l.stream().map((a) -> {
            a.setAlarm(1);
            return a;
        }).forEachOrdered((a) -> {
            this.update(a);
        });
    }

}

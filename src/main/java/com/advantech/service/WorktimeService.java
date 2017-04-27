/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WorktimeService {

    @Autowired
    private WorktimeDAO worktimeDAO;

    public List<Worktime> findAll() {
        return (List<Worktime>) worktimeDAO.findAll();
    }

    public List<Worktime> findAll(PageInfo info) {
        List l = worktimeDAO.findAll(info);
        Hibernate.initialize(l);
        return l;
    }

    public Worktime findByPrimaryKey(Object obj_id) {
        return (Worktime) worktimeDAO.findByPrimaryKey(obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... ids) {
        return worktimeDAO.findByPrimaryKeys(ids);
    }

    public Worktime findByModel(String modelName) {
        return worktimeDAO.findByModel(modelName);
    }

    public int insert(Worktime worktime) {
        return worktimeDAO.insert(worktime);
    }

    public int update(Worktime worktime) {
        return worktimeDAO.update(worktime);
    }

    public int update(List<Worktime> l) {
        for (Worktime w : l) {
            worktimeDAO.update(w);
        }
        return 1;
    }

    public int saveOrUpdate(List<Worktime> l) {
        for (int i = 0; i < l.size(); i++) {
            Worktime w = l.get(i);
            System.out.println("insert row: " + i + " \\Model: " + w.getModelName());
            worktimeDAO.saveOrUpdate(w);
        }
        return 1;
    }

    public int delete(List<Worktime> l) {
        for (Worktime m : l) {
            this.delete(m);
        }
        return 1;
    }

    public int delete(Object pojo) {
        return worktimeDAO.delete(pojo);
    }

}

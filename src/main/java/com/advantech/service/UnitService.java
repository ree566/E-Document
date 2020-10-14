/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.UnitDAO;
import com.advantech.model.Unit;
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
public class UnitService {

    @Autowired
    private UnitDAO unitDAO;

    public List<Unit> findAll() {
        return unitDAO.findAll();
    }

    public Unit findByPrimaryKey(Object obj_id) {
        return unitDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Unit unit) {
        return unitDAO.insert(unit);
    }

    public int update(Unit unit) {
        return unitDAO.update(unit);
    }

    public int delete(Unit unit) {
        return unitDAO.delete(unit);
    }

}

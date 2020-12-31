/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.UnitRepository;
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
    private UnitRepository repo;

    public List<Unit> findAll() {
        return repo.findAll();
    }

    public Unit findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public int insert(Unit unit) {
        repo.save(unit);
        return 1;
    }

    public int update(Unit unit) {
        repo.save(unit);
        return 1;
    }

    public int delete(Unit unit) {
        repo.delete(unit);
        return 1;
    }

}

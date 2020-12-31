/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.PendingRepository;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Pending;
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
public class PendingService {

    @Autowired
    private PendingRepository repo;

    public List<Pending> findAll() {
        return repo.findAll();
    }

    public List<Pending> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public Pending findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public int insert(Pending pending) {
        repo.save(pending);
        return 1;
    }

    public int update(Pending pending) {
        repo.save(pending);
        return 1;
    }

    public int delete(int id) {
        Pending pending = this.findByPrimaryKey(id);
        repo.delete(pending);
        return 1;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.PreAssyRepository;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.PreAssy;
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
public class PreAssyService {

    @Autowired
    private PreAssyRepository repo;

    public List<PreAssy> findAll() {
        return repo.findAll();
    }

    public List<PreAssy> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public PreAssy findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public int insert(PreAssy preAssy) {
        repo.save(preAssy);
        return 1;
    }

    public int update(PreAssy preAssy) {
        repo.save(preAssy);
        return 1;
    }

    public int delete(int id) {
        PreAssy preAssy = this.findByPrimaryKey(id);
        repo.delete(preAssy);
        return 1;
    }

}

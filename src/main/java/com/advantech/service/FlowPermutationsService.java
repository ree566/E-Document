/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowPermutations;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.FlowPermutationsRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FlowPermutationsService {

    @Autowired
    private FlowPermutationsRepository repo;

    public List<FlowPermutations> findAll() {
        return repo.findAll();
    }

    public List<FlowPermutations> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public FlowPermutations findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public String findLastCode() {
        return repo.findLastCode();
    }

    public int insert(FlowPermutations pojo) {
        repo.save(pojo);
        return 1;
    }

    public int update(FlowPermutations pojo) {
        repo.save(pojo);
        return 1;
    }

    public int delete(int id) {
        FlowPermutations f = this.findByPrimaryKey(id);
        repo.delete(f);
        return 1;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.FloorRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FloorService {

    @Autowired
    private FloorRepository repo;

    public List<Floor> findAll() {
        return repo.findAll();
    }

    public Floor findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<Floor> findByPrimaryKeys(Integer... id) {
        return repo.findAllById(Arrays.asList(id));
    }

    public int insert(Floor floor) {
        repo.save(floor);
        return 1;
    }

    public int update(Floor floor) {
        repo.save(floor);
        return 1;
    }

    public int delete(Floor floor) {
        repo.delete(floor);
        return 1;
    }

}

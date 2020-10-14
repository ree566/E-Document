/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.FloorRepository;
import java.util.Arrays;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FloorService {

    @Autowired
    private FloorRepository repo;

    public List<Floor> findByIdIn(Integer... ids) {
        return repo.findByIdIn(Arrays.asList(ids));
    }

    public List<Floor> findAll() {
        return repo.findAll();
    }

    public Floor getOne(Integer id) {
        return repo.getOne(id);
    }

    public <S extends Floor> S save(S s) {
        return repo.save(s);
    }

    public void delete(Floor t) {
        repo.delete(t);
    }

}

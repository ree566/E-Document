/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Floor;
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
public class FloorService {

    @Autowired
    private FloorDAO floorDAO;

    public List<Floor> findAll() {
        return (List<Floor>) floorDAO.findAll();
    }

    public Floor findByPrimaryKey(Object obj_id) {
        return (Floor) floorDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Floor floor) {
        return floorDAO.insert(floor);
    }

    public int update(Floor floor) {
        return floorDAO.update(floor);
    }

    public int delete(Floor floor) {
        return floorDAO.delete(floor);
    }

}

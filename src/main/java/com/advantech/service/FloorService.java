/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.FloorDAO;
import com.advantech.model.Floor;
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
public class FloorService {

    @Autowired
    private FloorDAO floorDAO;

    public List<Floor> findAll() {
        return floorDAO.findAll();
    }

    public Floor findByPrimaryKey(Object obj_id) {
        return floorDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Floor pojo) {
        return floorDAO.insert(pojo);
    }

    public int update(Floor pojo) {
        return floorDAO.update(pojo);
    }

    public int delete(Floor pojo) {
        return floorDAO.delete(pojo);
    }

}

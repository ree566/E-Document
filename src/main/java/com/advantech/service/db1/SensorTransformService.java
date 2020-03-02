/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.SensorTransformDAO;
import com.advantech.model.db1.SensorTransform;
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
public class SensorTransformService {

    @Autowired
    private SensorTransformDAO sensorTransformDAO;

    public List<SensorTransform> findAll() {
        return sensorTransformDAO.findAll();
    }

    public SensorTransform findByPrimaryKey(Object obj_id) {
        return sensorTransformDAO.findByPrimaryKey(obj_id);
    }

    public int insert(SensorTransform pojo) {
        return sensorTransformDAO.insert(pojo);
    }

    public int update(SensorTransform pojo) {
        return sensorTransformDAO.update(pojo);
    }

    public int delete(SensorTransform pojo) {
        return sensorTransformDAO.delete(pojo);
    }

}

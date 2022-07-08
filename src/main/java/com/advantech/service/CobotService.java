/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.service;

import com.advantech.dao.CobotDAO;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Cobot;
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
public class CobotService {

    @Autowired
    private CobotDAO dao;

    public List<Cobot> findAll() {
        return dao.findAll();
    }

    public List<Cobot> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    public Cobot findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(Cobot pojo) {
        return dao.insert(pojo);
    }

    public int update(Cobot pojo) {
        return dao.update(pojo);
    }

    public int delete(Cobot pojo) {
        return dao.delete(pojo);
    }

}

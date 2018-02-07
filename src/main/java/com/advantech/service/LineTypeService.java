/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.LineTypeDAO;
import com.advantech.model.LineType;
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
public class LineTypeService {

    @Autowired
    private LineTypeDAO lineTypeDAO;

    public List<LineType> findAll() {
        return lineTypeDAO.findAll();
    }

    public LineType findByPrimaryKey(Object obj_id) {
        return lineTypeDAO.findByPrimaryKey(obj_id);
    }

    public LineType findByName(String lineTypeName) {
        return lineTypeDAO.findByName(lineTypeName);
    }

    public int insert(LineType pojo) {
        return lineTypeDAO.insert(pojo);
    }

    public int update(LineType pojo) {
        return lineTypeDAO.update(pojo);
    }

    public int delete(LineType pojo) {
        return lineTypeDAO.delete(pojo);
    }

}

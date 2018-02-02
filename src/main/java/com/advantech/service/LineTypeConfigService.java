/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.LineTypeConfig;
import com.advantech.dao.LineTypeConfigDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class LineTypeConfigService {

    @Autowired
    private LineTypeConfigDAO lineTypeConfigDAO;

    public List<LineTypeConfig> findAll() {
        return lineTypeConfigDAO.findAll();
    }

    public LineTypeConfig findByPrimaryKey(Object obj_id) {
        return lineTypeConfigDAO.findByPrimaryKey(obj_id);
    }

    public List<LineTypeConfig> findByLineType(int lineType_id) {
        return lineTypeConfigDAO.findByLineType(lineType_id);
    }

    public List<LineTypeConfig> findWithLineType() {
        return lineTypeConfigDAO.findWithLineType();
    }

    public int insert(LineTypeConfig pojo) {
        return lineTypeConfigDAO.insert(pojo);
    }

    public int update(LineTypeConfig pojo) {
        return lineTypeConfigDAO.update(pojo);
    }

    public int delete(LineTypeConfig pojo) {
        return lineTypeConfigDAO.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.CellLineTypeRecordDAO;
import com.advantech.model.CellLineTypeRecord;
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
public class CellLineTypeRecordService {

    @Autowired
    private CellLineTypeRecordDAO cellLineTypeRecordDAO;

    public List<CellLineTypeRecord> findAll() {
        return cellLineTypeRecordDAO.findAll();
    }

    public CellLineTypeRecord findByPrimaryKey(Object obj_id) {
        return cellLineTypeRecordDAO.findByPrimaryKey(obj_id);
    }

    public int insert(CellLineTypeRecord pojo) {
        return cellLineTypeRecordDAO.insert(pojo);
    }

    public int update(CellLineTypeRecord pojo) {
        return cellLineTypeRecordDAO.update(pojo);
    }

    public int delete(CellLineTypeRecord pojo) {
        return cellLineTypeRecordDAO.delete(pojo);
    }

}

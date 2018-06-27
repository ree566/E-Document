/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.FqcTimeTempDAO;
import com.advantech.model.FqcTimeTemp;
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
public class FqcTimeTempService {

    @Autowired
    private FqcTimeTempDAO dao;

    public List<FqcTimeTemp> findAll() {
        return dao.findAll();
    }

    public FqcTimeTemp findByPrimaryKey(Integer obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public List<FqcTimeTemp> findByFqcIdIn(Integer... fqc_id) {
        return dao.findByFqcIdIn(fqc_id);
    }

    public int insert(FqcTimeTemp pojo) {
        return dao.insert(pojo);
    }

    public int update(FqcTimeTemp pojo) {
        return dao.update(pojo);
    }

    public int delete(FqcTimeTemp pojo) {
        return dao.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.FqcLineDAO;
import com.advantech.model.db1.FqcLine;
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
public class FqcLineService {

    @Autowired
    private FqcLineDAO fqcLineDAO;

    public List<FqcLine> findAll() {
        return fqcLineDAO.findAll();
    }

    public FqcLine findByPrimaryKey(Object obj_id) {
        return fqcLineDAO.findByPrimaryKey(obj_id);
    }

    public List<FqcLine> findBySitefloor(int floor_id) {
        return fqcLineDAO.findBySitefloor(floor_id);
    }

    public List<FqcLine> findBySitefloor(String floor_name) {
        return fqcLineDAO.findBySitefloor(floor_name);
    }

    public int insert(FqcLine pojo) {
        return fqcLineDAO.insert(pojo);
    }

    public int update(FqcLine pojo) {
        return fqcLineDAO.update(pojo);
    }

    public int delete(FqcLine pojo) {
        return fqcLineDAO.delete(pojo);
    }

}

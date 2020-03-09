/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.model.db1.BabLineProductivityExclude;
import com.advantech.dao.db1.BabLineProductivityExcludeDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng Please keep one setting per bab and station
 */
@Service
@Transactional
public class BabLineProductivityExcludeService {

    @Autowired
    private BabLineProductivityExcludeDAO babLineProductivityExcludeDAO;

    public List<BabLineProductivityExclude> findAll() {
        return babLineProductivityExcludeDAO.findAll();
    }

    public BabLineProductivityExclude findByPrimaryKey(Object obj_id) {
        return babLineProductivityExcludeDAO.findByPrimaryKey(obj_id);
    }

    public int insert(BabLineProductivityExclude pojo) {
        return babLineProductivityExcludeDAO.insert(pojo);
    }

    public int insert(List<BabLineProductivityExclude> l) {
        l.forEach((e) -> {
            this.insert(e);
        });
        return 1;
    }

    public int update(BabLineProductivityExclude pojo) {
        return babLineProductivityExcludeDAO.update(pojo);
    }

    public int delete(BabLineProductivityExclude pojo) {
        return babLineProductivityExcludeDAO.delete(pojo);
    }

}

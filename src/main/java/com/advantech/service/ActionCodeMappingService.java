/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.ActionCodeMapping;
import com.advantech.dao.ActionCodeMappingDAO;
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
public class ActionCodeMappingService {

    @Autowired
    private ActionCodeMappingDAO actionCodeMappingDAO;

    public List<ActionCodeMapping> findAll() {
        return actionCodeMappingDAO.findAll();
    }

    public ActionCodeMapping findByPrimaryKey(Object obj_id) {
        return actionCodeMappingDAO.findByPrimaryKey(obj_id);
    }

    public List<ActionCodeMapping> findByActionCode(int ac_id) {
        return actionCodeMappingDAO.findByActionCode(ac_id);
    }

    public int insert(ActionCodeMapping pojo) {
        return actionCodeMappingDAO.insert(pojo);
    }

    public int update(ActionCodeMapping pojo) {
        return actionCodeMappingDAO.update(pojo);
    }

    public int delete(ActionCodeMapping pojo) {
        return actionCodeMappingDAO.delete(pojo);
    }

}

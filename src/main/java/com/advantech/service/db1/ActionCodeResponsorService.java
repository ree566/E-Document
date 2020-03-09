/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.model.db1.ActionCodeResponsor;
import com.advantech.dao.db1.ActionCodeResponsorDAO;
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
public class ActionCodeResponsorService {

    @Autowired
    private ActionCodeResponsorDAO actionCodeResponsorDAO;

    public List<ActionCodeResponsor> findAll() {
        return actionCodeResponsorDAO.findAll();
    }

    public ActionCodeResponsor findByPrimaryKey(Object obj_id) {
        return actionCodeResponsorDAO.findByPrimaryKey(obj_id);
    }

    public List<ActionCodeResponsor> findByActionCode(int ac_id) {
        return actionCodeResponsorDAO.findByActionCode(ac_id);
    }

    public int insert(ActionCodeResponsor pojo) {
        return actionCodeResponsorDAO.insert(pojo);
    }

    public int update(ActionCodeResponsor pojo) {
        return actionCodeResponsorDAO.update(pojo);
    }

    public int delete(ActionCodeResponsor pojo) {
        return actionCodeResponsorDAO.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.ActionCodeDAO;
import com.advantech.model.db1.ActionCode;
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
public class ActionCodeService {

    @Autowired
    private ActionCodeDAO actionCodeDAO;

    public List<ActionCode> findAll() {
        return actionCodeDAO.findAll();
    }

    public ActionCode findByPrimaryKey(Object obj_id) {
        return actionCodeDAO.findByPrimaryKey(obj_id);
    }

    public List<ActionCode> findByPrimaryKeys(Integer... obj_ids) {
        return actionCodeDAO.findByPrimaryKeys(obj_ids);
    }

    public int insert(ActionCode pojo) {
        return actionCodeDAO.insert(pojo);
    }

    public int update(ActionCode pojo) {
        return actionCodeDAO.update(pojo);
    }

    public int delete(ActionCode pojo) {
        return actionCodeDAO.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.ActionCodeDAO;
import java.util.List;
import java.util.Map;
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

    public List<Map> findAll() {
        return actionCodeDAO.findAll();
    }

    public boolean insert(int cm_id, List<String> actionCodes) {
        return actionCodeDAO.insert(cm_id, actionCodes);
    }

    public boolean delete(int cm_id) {
        return actionCodeDAO.delete(cm_id);
    }

}

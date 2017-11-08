/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.ActionCodeMapping;
import com.advantech.dao.ActionCodeMappingDAO;
import java.util.List;
import java.util.Map;
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

    public List<ActionCodeMapping> getActionCodeMapping() {
        return actionCodeMappingDAO.getActionCodeMapping();
    }

    public List<Map> getActionCodeMapping1() {
        return actionCodeMappingDAO.getActionCodeMapping1();
    }

    public List<ActionCodeMapping> getActionCodeMapping(int id) {
        return actionCodeMappingDAO.getActionCodeMapping(id);
    }

    public List<ActionCodeMapping> getActionCodeMappingByActionCode(int ac_id) {
        return actionCodeMappingDAO.getActionCodeMappingByActionCode(ac_id);
    }
}

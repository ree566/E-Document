/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.ErrorCodeDAO;
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
public class ErrorCodeService {

    @Autowired
    private ErrorCodeDAO errorCodeDAO;

    public List<Map> findAll() {
        return errorCodeDAO.findAll();
    }

    public List<Map> findByCountermeasure(int cm_id) {
        return errorCodeDAO.findByCountermeasure(cm_id);
    }

}

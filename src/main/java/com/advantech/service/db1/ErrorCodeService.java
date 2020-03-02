/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.ErrorCodeDAO;
import com.advantech.model.db1.ActionCode;
import com.advantech.model.db1.ErrorCode;
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
public class ErrorCodeService {

    @Autowired
    private ErrorCodeDAO errorCodeDAO;

    public List<ErrorCode> findAll() {
        return errorCodeDAO.findAll();
    }

    public ErrorCode findByPrimaryKey(Object obj_id) {
        return errorCodeDAO.findByPrimaryKey(obj_id);
    }
    
    public List<ErrorCode> findByPrimaryKeys(Integer... obj_ids) {
        return errorCodeDAO.findByPrimaryKeys(obj_ids);
    }

    public int insert(ErrorCode pojo) {
        return errorCodeDAO.insert(pojo);
    }

    public int update(ErrorCode pojo) {
        return errorCodeDAO.update(pojo);
    }

    public int delete(ErrorCode pojo) {
        return errorCodeDAO.delete(pojo);
    }

}

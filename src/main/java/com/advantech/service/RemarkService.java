/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Remark;
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
public class RemarkService {

    @Autowired
    private RemarkDAO remarkDAO;

    public List<Remark> findAll() {
        return remarkDAO.findAll();
    }

    public List<Remark> findAll(PageInfo info) {
        return remarkDAO.findAll(info);
    }

    public Remark findByPrimaryKey(Object obj_id) {
        return remarkDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Remark remark) {
        return remarkDAO.insert(remark);
    }

    public int update(Remark remark) {
        return remarkDAO.update(remark);
    }

    public int delete(int id) {
        Remark remark = this.findByPrimaryKey(id);
        return remarkDAO.delete(remark);
    }

}

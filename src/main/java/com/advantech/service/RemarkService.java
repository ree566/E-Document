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
public class RemarkService extends BasicServiceImpl<Integer, Remark> {

    @Autowired
    private RemarkDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<Remark> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    public int delete(int id) {
        Remark pojo = dao.findByPrimaryKey(id);
        dao.delete(pojo);
        return 1;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowPermutations;
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
public class FlowPermutationsService extends BasicServiceImpl<Integer, FlowPermutations> {

    @Autowired
    private FlowPermutationsDAO dao;

    @Override
    protected BasicDAOImpl getDao() {
        return this.dao;
    }

    public List<FlowPermutations> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    public String findLastCode() {
        return dao.findLastCode();
    }

    public int delete(int id) {
        FlowPermutations f = dao.findByPrimaryKey(id);
        return dao.delete(f);
    }

}

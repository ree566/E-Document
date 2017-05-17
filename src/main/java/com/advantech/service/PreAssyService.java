/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.helper.PageInfo;
import com.advantech.model.PreAssy;
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
public class PreAssyService {

    @Autowired
    private PreAssyDAO preAssyDAO;

    public List<PreAssy> findAll() {
        return (List<PreAssy>) preAssyDAO.findAll();
    }

    public List<PreAssy> findAll(PageInfo info) {
        return preAssyDAO.findAll(info);
    }

    public PreAssy findByPrimaryKey(Object obj_id) {
        return (PreAssy) preAssyDAO.findByPrimaryKey(obj_id);
    }

    public int insert(PreAssy preAssy) {
        return preAssyDAO.insert(preAssy);
    }

    public int update(PreAssy preAssy) {
        return preAssyDAO.update(preAssy);
    }

    public int delete(int id) {
        PreAssy preAssy = this.findByPrimaryKey(id);
        return preAssyDAO.delete(preAssy);
    }

}

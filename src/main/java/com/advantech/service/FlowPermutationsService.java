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
public class FlowPermutationsService {

    @Autowired
    private FlowPermutationsDAO dao;

    public List<FlowPermutations> findAll() {
        return dao.findAll();
    }

    public List<FlowPermutations> findAll(PageInfo info) {
        return dao.findAll(info);
    }

    public FlowPermutations findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(FlowPermutations pojo) {
        List<FlowPermutations> l = dao.findAll();
        FlowPermutations lastRow = l.get(l.size() - 1);
        String code = lastRow.getCode();
        String generateCode = "R" + (Integer.parseInt(code.replace("R", "")) + 1);
        pojo.setCode(generateCode);
        return dao.insert(pojo);
    }

    public int update(FlowPermutations pojo) {
        return dao.update(pojo);
    }

    public int delete(int id) {
        FlowPermutations f = dao.findByPrimaryKey(id);
        return dao.delete(f);
    }

}

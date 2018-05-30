/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.ModelSopRemarkDAO;
import com.advantech.model.Line;
import com.advantech.model.ModelSopRemark;
import com.advantech.model.ModelSopRemarkDetail;
import com.advantech.model.User;
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
public class ModelSopRemarkService {

    @Autowired
    private ModelSopRemarkDAO modelSopRemarkDAO;

    public List<ModelSopRemark> findAll() {
        return modelSopRemarkDAO.findAll();
    }

    public ModelSopRemark findByPrimaryKey(Object obj_id) {
        return modelSopRemarkDAO.findByPrimaryKey(obj_id);
    }

    public List<ModelSopRemark> findByUser(User user) {
        return modelSopRemarkDAO.findByUser(user);
    }

    public List<Line> findUseLine(int id) {
        return modelSopRemarkDAO.findUseLine(id);
    }

    public List<ModelSopRemarkDetail> findDetail(int id) {
        return modelSopRemarkDAO.findDetail(id);
    }

    public int insert(ModelSopRemark pojo) {
        return modelSopRemarkDAO.insert(pojo);
    }

    public int update(ModelSopRemark pojo) {
        return modelSopRemarkDAO.update(pojo);
    }

    public int delete(ModelSopRemark pojo) {
        return modelSopRemarkDAO.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.ModelSopRemarkDAO;
import com.advantech.model.db1.Line;
import com.advantech.model.db1.ModelSopRemark;
import com.advantech.model.db1.ModelSopRemarkDetail;
import com.advantech.model.db1.User;
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

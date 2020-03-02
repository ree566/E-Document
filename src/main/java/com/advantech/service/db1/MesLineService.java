/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.MesLineDAO;
import com.advantech.model.db1.MesLine;
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
public class MesLineService {

    @Autowired
    private MesLineDAO lineDAO;

    public List<MesLine> findAll() {
        return lineDAO.findAll();
    }

    public MesLine findByPrimaryKey(Object obj_id) {
        return lineDAO.findByPrimaryKey(obj_id);
    }

    public int insert(MesLine pojo) {
        return lineDAO.insert(pojo);
    }

    public int update(MesLine pojo) {
        return lineDAO.update(pojo);
    }

    public int delete(MesLine pojo) {
        return lineDAO.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Line;
import com.advantech.dao.LineDAO;
import com.advantech.model.LineStatus;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class LineService {

    @Autowired
    private LineDAO lineDAO;

    public List<Line> findAll() {
        return lineDAO.findAll();
    }

    public Line findByPrimaryKey(Object obj_id) {
        return lineDAO.findByPrimaryKey(obj_id);
    }

    public List<Line> findBySitefloor(int floor_id) {
        return lineDAO.findBySitefloor(floor_id);
    }

    public int insert(Line pojo) {
        return lineDAO.insert(pojo);
    }

    public int update(Line pojo) {
        return lineDAO.update(pojo);
    }
    
    public void closeAll(){
        List<Line> l = lineDAO.findAll();
        l.forEach((line) -> {
            line.setLineStatus(LineStatus.CLOSE);
            this.update(line);
        });
    }

    public int delete(Line pojo) {
        return lineDAO.delete(pojo);
    }

}

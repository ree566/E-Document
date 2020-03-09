package com.advantech.service.db1;

import com.advantech.model.db1.CountermeasureType;
import com.advantech.dao.db1.CountermeasureTypeDAO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class CountermeasureTypeService {

    @Autowired
    private CountermeasureTypeDAO countermeasureTypeDAO;

    public List<CountermeasureType> findAll() {
        return countermeasureTypeDAO.findAll();
    }

    public CountermeasureType findByPrimaryKey(Object obj_id) {
        return countermeasureTypeDAO.findByPrimaryKey(obj_id);
    }

    public CountermeasureType findByName(String name) {
        return countermeasureTypeDAO.findByName(name);
    }

    public int insert(CountermeasureType pojo) {
        countermeasureTypeDAO.insert(pojo);
        return 1;
    }

    public int update(CountermeasureType pojo) {
        return countermeasureTypeDAO.update(pojo);
    }

    public int delete(CountermeasureType pojo) {
        return countermeasureTypeDAO.delete(pojo);
    }

}

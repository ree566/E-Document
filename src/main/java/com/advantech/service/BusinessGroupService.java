/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.BusinessGroup;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.advantech.repo.BusinessGroupRepository;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class BusinessGroupService {

    @Autowired
    private BusinessGroupRepository repo;

    public List<BusinessGroup> findAll() {
        return repo.findAll();
    }

    public BusinessGroup findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public int insert(BusinessGroup pojo) {
        repo.save(pojo);
        return 1;
    }

    public int update(BusinessGroup pojo) {
        repo.save(pojo);
        return 1;
    }

    public int delete(BusinessGroup pojo) {
        repo.delete(pojo);
        return 1;
    }

}

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

    public BusinessGroup getOne(Integer id) {
        return repo.getOne(id);
    }

    public <S extends BusinessGroup> S save(S s) {
        return repo.save(s);
    }

    public void delete(BusinessGroup t) {
        repo.delete(t);
    }

}

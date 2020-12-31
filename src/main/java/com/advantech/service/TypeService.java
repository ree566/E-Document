/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.TypeRepository;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Type;
import java.util.Arrays;
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
public class TypeService {

    @Autowired
    private TypeRepository repo;

    public List<Type> findAll() {
        return repo.findAll();
    }

    public List<Type> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public Type findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public List<Type> findByPrimaryKeys(Integer... id) {
        return repo.findAllById(Arrays.asList(id));
    }

    public int insert(Type type) {
        repo.save(type);
        return 1;
    }

    public int update(Type type) {
        repo.save(type);
        return 1;
    }

    public int delete(int id) {
        Type type = this.findByPrimaryKey(id);
        repo.delete(type);
        return 1;
    }

}

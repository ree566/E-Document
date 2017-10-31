package com.advantech.service;

import com.advantech.entity.BAB;
import com.advantech.model.BABPreDAO;
import java.util.List;
import javax.transaction.Transactional;
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
public class BABPreService {

    private BABPreDAO babPreDAO;

    public List<BAB> findAll() {
        return babPreDAO.findAll();
    }

    public boolean insert(BAB bab) {
        return babPreDAO.insert(bab);
    }

    public boolean update(BAB bab) {
        return babPreDAO.update(bab);
    }

    public boolean delete(int id) {
        return babPreDAO.delete(id);
    }
}

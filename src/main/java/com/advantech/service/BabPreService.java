package com.advantech.service;

import com.advantech.model.Bab;
import com.advantech.dao.BabPreDAO;
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
public class BabPreService {

    private BabPreDAO babPreDAO;

    public List<Bab> findAll() {
        return babPreDAO.findAll();
    }

    public boolean insert(Bab bab) {
        return babPreDAO.insert(bab);
    }

    public boolean update(Bab bab) {
        return babPreDAO.update(bab);
    }

    public boolean delete(int id) {
        return babPreDAO.delete(id);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Model;
import com.advantech.model.*;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class ModelService {

    private final ModelDAO modelDAO;

    public ModelService() {
        modelDAO = new ModelDAO();
    }

    public List<Map> getAll() {
        return modelDAO.getAll();
    }

    public List<Map> getOne(Model model) {
        return modelDAO.getOne(model);
    }

    public boolean add(Model model) {
        return modelDAO.add(model);
    }

    public boolean update(Model model) {
        return modelDAO.update(model);
    }

    public boolean delete(Model model) {
        return modelDAO.delete(model);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.CELL;
import com.advantech.model.CELLDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CELLService {

    private final CELLDAO cellDAO;

    protected CELLService() {
        cellDAO = new CELLDAO();
    }

    public List<CELL> findAll(){
        return cellDAO.findAll();
    }
    
    public CELL findOne(int id){
        return cellDAO.findOne(id);
    }

}

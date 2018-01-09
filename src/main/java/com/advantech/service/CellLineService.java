/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.CellLine;
import com.advantech.dao.CellLineDAO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class CellLineService {

    @Autowired
    private CellLineDAO cellDAO;

    private final int LINE_OPEN_SIGN = 1;
    private final int LINE_CLOSE_SIGN = 0;

    public List<CellLine> findAll() {
        return cellDAO.findAll();
    }

    public CellLine findOne(int id) {
        return cellDAO.findOne(id);
    }

    public List<CellLine> findBySitefloor(int sitefloor) {
        return cellDAO.findBySitefloor(sitefloor);
    }

    public boolean login(int id) {
        return cellDAO.updateStatus(id, LINE_OPEN_SIGN);
    }

    public boolean logout(int id) {
        return cellDAO.updateStatus(id, LINE_CLOSE_SIGN);
    }

    public boolean closeAll() {
        return cellDAO.updateStatus(LINE_CLOSE_SIGN);
    }

}

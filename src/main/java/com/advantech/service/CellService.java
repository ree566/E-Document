/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Cell;
import com.advantech.model.CellDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class CellService {

    private final CellDAO cellProcessDAO;

    protected CellService() {
        cellProcessDAO = new CellDAO();
    }

    public List<Cell> getCellProcess() {
        return cellProcessDAO.getCellProcess();
    }

    public List<Cell> getCellProcess(String PO) {
        return cellProcessDAO.getCellProcess(PO);
    }

    public List<Map> getCellPerPcsHistory(String PO) {
        return cellProcessDAO.getCellPerPcsHistory(PO);
    }

    public List<Map> getCellPerPcsHistory(String PO, int maxinumPcs) {
        List l = cellProcessDAO.getCellPerPcsHistory(PO);
        return l.isEmpty() ? l : l.subList(0, maxinumPcs);
    }

    public boolean insertCellProcess(List<Cell> l) {
        return cellProcessDAO.insertCellProcess(l);
    }

    public boolean deleteCellProcess(String PO) {
        return cellProcessDAO.deleteCellProcess(PO);
    }
}

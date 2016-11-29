/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.CellProcess;
import com.advantech.model.CellProcessDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class CellProcessService {

    private final CellProcessDAO cellProcessDAO;

    protected CellProcessService() {
        cellProcessDAO = new CellProcessDAO();
    }

    public List<CellProcess> getCellProcess() {
        return cellProcessDAO.getCellProcess();
    }

    public List<CellProcess> getCellProcess(String PO) {
        return cellProcessDAO.getCellProcess(PO);
    }

    public boolean insertCellProcess(List<CellProcess> l) {
        return cellProcessDAO.insertCellProcess(l);
    }

    public boolean deleteCellProcess(String PO) {
        return cellProcessDAO.deleteCellProcess(PO);
    }
}

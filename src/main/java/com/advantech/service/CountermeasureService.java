package com.advantech.service;

import com.advantech.entity.Countermeasure;
import com.advantech.model.CountermeasureDAO;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
public class CountermeasureService {

    private final CountermeasureDAO countermeasureDAO;

    protected CountermeasureService() {
        countermeasureDAO = new CountermeasureDAO();
    }

    public List<Countermeasure> getCountermeasure() {
        return countermeasureDAO.getCountermeasures();
    }

    public List<Countermeasure> getCountermeasure(int BABid) {
        return countermeasureDAO.getCountermeasure(BABid);
    }

    public List getErrorCode() {
        return countermeasureDAO.getErrorCode();
    }

    public boolean insertCountermeasure(int BABid, int errorCode_id, String reason, String solution, String editor) {
        return countermeasureDAO.insertCountermeasure(BABid, errorCode_id, reason, solution, editor);
    }

    public boolean updateCountermeasure(int id, int errorCode_id, String reason, String solution, String editor) {
        if (this.getCountermeasure(id).isEmpty()) {
            return this.insertCountermeasure(id, errorCode_id, reason, solution, editor);
        } else {
            return countermeasureDAO.updateCountermeasure(id, errorCode_id, reason, solution, editor);
        }
    }

    public boolean deleteCountermeasure(int id) {
        return countermeasureDAO.deleteCountermeasure(id);
    }

}

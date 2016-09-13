package com.advantech.service;

import com.advantech.entity.Countermeasure;
import static com.advantech.model.BasicDAO.queryForMapList;
import com.advantech.model.CountermeasureDAO;
import java.util.List;
import java.util.Map;

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

    public Countermeasure getCountermeasure(int BABid) {
        return countermeasureDAO.getCountermeasure(BABid);
    }

    public List<Map> getCountermeasureView() {
        return countermeasureDAO.getCountermeasureView();
    }

    public List<Map> getErrorCode() {
        return countermeasureDAO.getErrorCode();
    }

    public List<Map> getErrorCode(int cm_id) {
        return countermeasureDAO.getErrorCode(cm_id);
    }

    public List<Map> getActionCode() {
        return countermeasureDAO.getActionCode();
    }

    public List<Map> getEditor(int cm_id) {
        return countermeasureDAO.getEditor(cm_id);
    }

    public boolean insertCountermeasure(int BABid, List<String> errorCode_id, String solution, String editor) {
        return countermeasureDAO.insertCountermeasure(BABid, solution, errorCode_id, editor);
    }

    public boolean updateCountermeasure(int id, List<String> errorCode_id, String solution, String editor) {
        if (this.getCountermeasure(id) == null) {
            return this.insertCountermeasure(id, errorCode_id, solution, editor);
        } else {
            return countermeasureDAO.updateCountermeasure(id, solution, errorCode_id, editor);
        }
    }

    public boolean deleteCountermeasure(int id) {
        return countermeasureDAO.deleteCountermeasure(id);
    }

}

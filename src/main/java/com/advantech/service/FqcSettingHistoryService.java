/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.FqcSettingHistoryDAO;
import com.advantech.model.Fqc;
import com.advantech.model.FqcSettingHistory;
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
public class FqcSettingHistoryService {

    @Autowired
    private FqcSettingHistoryDAO fqcSettingHistoryDAO;

    public List<FqcSettingHistory> findAll() {
        return fqcSettingHistoryDAO.findAll();
    }

    public FqcSettingHistory findByPrimaryKey(Object obj_id) {
        return fqcSettingHistoryDAO.findByPrimaryKey(obj_id);
    }

    public List<FqcSettingHistory> findProcessing() {
        return fqcSettingHistoryDAO.findProcessing();
    }

    public List<FqcSettingHistory> findProcessing(int fqcLine_id) {
        return fqcSettingHistoryDAO.findProcessing(fqcLine_id);
    }

    public FqcSettingHistory findByFqc(Fqc fqc) {
        return fqcSettingHistoryDAO.findByFqc(fqc);
    }

    public int insert(FqcSettingHistory pojo) {
        return fqcSettingHistoryDAO.insert(pojo);
    }

    public int update(FqcSettingHistory pojo) {
        return fqcSettingHistoryDAO.update(pojo);
    }

    public int delete(FqcSettingHistory pojo) {
        return fqcSettingHistoryDAO.delete(pojo);
    }

}

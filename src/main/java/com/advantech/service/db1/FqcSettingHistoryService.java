/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.FqcSettingHistoryDAO;
import com.advantech.model.db1.Fqc;
import com.advantech.model.db1.FqcSettingHistory;
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

    public List<FqcSettingHistory> findByFqcIdIn(Integer... ids) {
        return fqcSettingHistoryDAO.findByFqcIdIn(ids);
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

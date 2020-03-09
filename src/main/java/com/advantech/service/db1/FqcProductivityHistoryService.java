/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.FqcProductivityHistoryDAO;
import com.advantech.model.db1.FqcProductivityHistory;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FqcProductivityHistoryService {

    @Autowired
    private FqcProductivityHistoryDAO fqcProducitvityHistoryDAO;

    public List<FqcProductivityHistory> findAll() {
        return fqcProducitvityHistoryDAO.findAll();
    }

    public FqcProductivityHistory findByPrimaryKey(Object obj_id) {
        return fqcProducitvityHistoryDAO.findByPrimaryKey(obj_id);
    }

    public List<Map> findComplete() {
        return fqcProducitvityHistoryDAO.findComplete(null, null);
    }

    public List<Map> findComplete(DateTime sD, DateTime eD) {
        return fqcProducitvityHistoryDAO.findComplete(sD, eD);
    }

    public FqcProductivityHistory findByFqc(int fqc_id) {
        return fqcProducitvityHistoryDAO.findByFqc(fqc_id);
    }

    public int insert(FqcProductivityHistory pojo) {
        return fqcProducitvityHistoryDAO.insert(pojo);
    }

    public int update(FqcProductivityHistory pojo) {
        return fqcProducitvityHistoryDAO.update(pojo);
    }

    public int delete(FqcProductivityHistory pojo) {
        return fqcProducitvityHistoryDAO.delete(pojo);
    }

}

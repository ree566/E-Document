/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.FqcProducitvityHistoryDAO;
import com.advantech.model.FqcProducitvityHistory;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FqcProducitvityHistoryService {

    @Autowired
    private FqcProducitvityHistoryDAO fqcProducitvityHistoryDAO;

    public List<FqcProducitvityHistory> findAll() {
        return fqcProducitvityHistoryDAO.findAll();
    }

    public FqcProducitvityHistory findByPrimaryKey(Object obj_id) {
        return fqcProducitvityHistoryDAO.findByPrimaryKey(obj_id);
    }

    public List<Map> findTodayWithComplete() {
        return fqcProducitvityHistoryDAO.findTodayWithComplete();
    }

    public int insert(FqcProducitvityHistory pojo) {
        return fqcProducitvityHistoryDAO.insert(pojo);
    }

    public int update(FqcProducitvityHistory pojo) {
        return fqcProducitvityHistoryDAO.update(pojo);
    }

    public int delete(FqcProducitvityHistory pojo) {
        return fqcProducitvityHistoryDAO.delete(pojo);
    }

}

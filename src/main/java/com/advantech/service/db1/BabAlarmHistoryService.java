/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.BabAlarmHistoryDAO;
import com.advantech.model.db1.BabAlarmHistory;
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
public class BabAlarmHistoryService {

    @Autowired
    private BabAlarmHistoryDAO babAlarmHistoryDAO;

    public List<BabAlarmHistory> findAll() {
        return babAlarmHistoryDAO.findAll();
    }

    public BabAlarmHistory findByPrimaryKey(Object obj_id) {
        return babAlarmHistoryDAO.findByPrimaryKey(obj_id);
    }

    public BabAlarmHistory findByBab(int bab_id) {
        return babAlarmHistoryDAO.findByBab(bab_id);
    }

    public int insert(BabAlarmHistory pojo) {
        return babAlarmHistoryDAO.insert(pojo);
    }

    public int update(BabAlarmHistory pojo) {
        return babAlarmHistoryDAO.update(pojo);
    }

    public int delete(BabAlarmHistory pojo) {
        return babAlarmHistoryDAO.delete(pojo);
    }

}

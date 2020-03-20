/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.PrepareScheduleEndtimeSettingDAO;
import com.advantech.model.db1.PrepareScheduleEndtimeSetting;
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
public class PrepareScheduleEndtimeSettingService {

    @Autowired
    private PrepareScheduleEndtimeSettingDAO dao;

    public List<PrepareScheduleEndtimeSetting> findAll() {
        return dao.findAll();
    }

    public PrepareScheduleEndtimeSetting findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public PrepareScheduleEndtimeSetting findByWeekOfWeekyear(int weekOfWeekyear) {
        return dao.findByWeekOfWeekyear(weekOfWeekyear);
    }

    public int insert(PrepareScheduleEndtimeSetting pojo) {
        return dao.insert(pojo);
    }

    public int update(PrepareScheduleEndtimeSetting pojo) {
        return dao.update(pojo);
    }

    public int delete(PrepareScheduleEndtimeSetting pojo) {
        return dao.delete(pojo);
    }

}

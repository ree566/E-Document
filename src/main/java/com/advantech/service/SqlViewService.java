/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.SqlViewDAO;
import com.advantech.model.view.BabAvg;
import com.advantech.model.view.BabLastGroupStatus;
import com.advantech.model.view.SensorCurrentGroupStatus;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.model.view.Worktime;
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
public class SqlViewService {

    @Autowired
    private SqlViewDAO sqlViewDAO;

    public List<BabAvg> findBabAvg(int bab_id) {
        return sqlViewDAO.findBabAvg(bab_id);
    }

    public Worktime findWorktimeByModelName(String modelName) {
        return sqlViewDAO.findWorktimeByModelName(modelName);
    }

    public UserInfoRemote findUserInfoRemote(String jobnumber) {
        return sqlViewDAO.findUserInfoRemote(jobnumber);
    }

    public List<BabLastGroupStatus> findBabLastGroupStatus(int bab_id) {
        return sqlViewDAO.findBabLastGroupStatus(bab_id);
    }

    public List<SensorCurrentGroupStatus> findSensorCurrentGroupStatus(int bab_id) {
        return sqlViewDAO.findSensorCurrentGroupStatus(bab_id);
    }

}

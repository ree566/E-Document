/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabSensorLoginRecordDAO;
import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.SensorTransform;
import static com.google.common.base.Preconditions.checkArgument;
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
public class BabSensorLoginRecordService {

    @Autowired
    private BabSensorLoginRecordDAO babSensorLoginRecordDAO;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    public List<BabSensorLoginRecord> findAll() {
        return babSensorLoginRecordDAO.findAll();
    }

    public BabSensorLoginRecord findByPrimaryKey(Object obj_id) {
        return babSensorLoginRecordDAO.findByPrimaryKey(obj_id);
    }

    public BabSensorLoginRecord findBySensor(String tagName) {
        return babSensorLoginRecordDAO.findBySensor(tagName);
    }

    public List<BabSensorLoginRecord> findByLine(int line_id) {
        return babSensorLoginRecordDAO.findByLine(line_id);
    }

    public int insert(String tagName, String jobnumber) {
        BabSensorLoginRecord recCheck1 = this.findBySensor(tagName);
        checkArgument(recCheck1 == null, "This sensor is already logged in by other user");
        checkJobnumberIsExist(jobnumber);
        return babSensorLoginRecordDAO.insert(new BabSensorLoginRecord(new SensorTransform(tagName), jobnumber));
    }

    public int update(BabSensorLoginRecord pojo) {
        return babSensorLoginRecordDAO.update(pojo);
    }

    public int changeUser(String jobnumber, String tagName) {
        BabSensorLoginRecord loginRec = this.findBySensor(tagName);
        checkArgument(loginRec != null, "Can't find login record on tagName " + tagName);
        checkJobnumberIsExist(jobnumber);
        loginRec.setJobnumber(jobnumber);
        this.update(loginRec);

        BabSettingHistory setting = babSettingHistoryService.findProcessingByTagName(tagName);

        if (setting != null) {
            setting.setJobnumber(jobnumber);
            babSettingHistoryService.update(setting);
        }
        return 1;
    }

    private void checkJobnumberIsExist(String jobnumber) {
        List<BabSensorLoginRecord> rec = this.babSensorLoginRecordDAO.findByJobnumber(jobnumber);
        checkArgument(rec.isEmpty(), "This user is already logged in on other position");
    }

    public int delete(BabSensorLoginRecord pojo) {
        return babSensorLoginRecordDAO.delete(pojo);
    }

    public int init() {
        List<BabSensorLoginRecord> l = babSensorLoginRecordDAO.findAll();
        l.forEach(b -> {
            babSensorLoginRecordDAO.delete(b);
        });
        return 1;
    }

}

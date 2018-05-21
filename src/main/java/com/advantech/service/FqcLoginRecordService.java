/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.FqcLoginRecordDAO;
import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.FqcLoginRecord;
import com.advantech.model.SensorTransform;
import static com.google.common.base.Preconditions.checkArgument;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FqcLoginRecordService {

    @Autowired
    private FqcLoginRecordDAO fqcLoginRecordDAO;

    public List<FqcLoginRecord> findAll() {
        return fqcLoginRecordDAO.findAll();
    }

    public FqcLoginRecord findByPrimaryKey(Object obj_id) {
        return fqcLoginRecordDAO.findByPrimaryKey(obj_id);
    }

    public FqcLoginRecord findByFqcLine(int fqcLine_id) {
        return fqcLoginRecordDAO.findByFqcLine(fqcLine_id);
    }

    public int insert(FqcLoginRecord pojo) {
        List<FqcLoginRecord> l = this.findAll();
        FqcLoginRecord existRecord = l.stream()
                .filter(p -> Objects.equals(p.getJobnumber(), pojo.getJobnumber())
                || Objects.equals(p.getFqcLine().getId(), pojo.getFqcLine().getId()))
                .findFirst().orElse(null);

        checkArgument(existRecord == null, "Jobnumber or FqcLine is already in fqcRecord.");
        return fqcLoginRecordDAO.insert(pojo);
    }

    public int update(FqcLoginRecord pojo) {
        return fqcLoginRecordDAO.update(pojo);
    }

    public int delete(FqcLoginRecord pojo) {
        return fqcLoginRecordDAO.delete(pojo);
    }

}

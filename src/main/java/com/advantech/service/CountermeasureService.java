package com.advantech.service;

import com.advantech.model.Countermeasure;
import com.advantech.dao.CountermeasureDAO;
import com.advantech.model.Bab;
import com.advantech.model.CountermeasureSopRecord;
import com.advantech.model.ReplyStatus;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class CountermeasureService {

    @Autowired
    private CountermeasureDAO countermeasureDAO;

    @Autowired
    private BabService babService;

    @Autowired
    private CountermeasureSopRecordService countermeasureSopRecordService;

    public List<Countermeasure> findAll() {
        return countermeasureDAO.findAll();
    }

    public Countermeasure findByPrimaryKey(Object obj_id) {
        return countermeasureDAO.findByPrimaryKey(obj_id);
    }

    public Countermeasure findByBabAndTypeName(int bab_id, String typeName) {
        return countermeasureDAO.findByBabAndTypeName(bab_id, typeName);
    }

    public int insert(Countermeasure pojo) {
        countermeasureDAO.insert(pojo);
        if ("Bab_Abnormal_LineBalance".equals(pojo.getCountermeasureType().getName())) {
            Bab b = pojo.getBab();
            b.setReplyStatus(ReplyStatus.REPLIED);
            babService.update(b);
        }
        return 1;
    }

    public int insert(Countermeasure c, String sop) {
        this.insert(c);
        countermeasureSopRecordService.insert(new CountermeasureSopRecord(c, sop));
        return 1;
    }

    public int update(Countermeasure pojo) {
        return countermeasureDAO.update(pojo);
    }

    public int update(Countermeasure c, String sop) {
        this.update(c);
        CountermeasureSopRecord rec = countermeasureSopRecordService.findByCountermeasure(c.getId());
        if (rec == null) {
            countermeasureSopRecordService.insert(new CountermeasureSopRecord(c, sop));
        } else {
            rec.setSop(sop);
            countermeasureSopRecordService.update(rec);
        }
        return 1;
    }

    public int delete(Countermeasure pojo) {
        return countermeasureDAO.delete(pojo);
    }

}

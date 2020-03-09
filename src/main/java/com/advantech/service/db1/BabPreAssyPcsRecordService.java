/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.BabPreAssyPcsRecordDAO;
import com.advantech.model.db1.BabPreAssyPcsRecord;
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
public class BabPreAssyPcsRecordService {

    @Autowired
    private BabPreAssyPcsRecordDAO babPcsDetailHistoryDAO;

    public List<BabPreAssyPcsRecord> findAll() {
        return babPcsDetailHistoryDAO.findAll();
    }

    public BabPreAssyPcsRecord findByPrimaryKey(Object obj_id) {
        return babPcsDetailHistoryDAO.findByPrimaryKey(obj_id);
    }

    public int insert(BabPreAssyPcsRecord pojo) {
        return babPcsDetailHistoryDAO.insert(pojo);
    }

    public int insert(List<BabPreAssyPcsRecord> l) {
        return babPcsDetailHistoryDAO.insert(l);
    }

    public int update(BabPreAssyPcsRecord pojo) {
        return babPcsDetailHistoryDAO.update(pojo);
    }

    public int delete(BabPreAssyPcsRecord pojo) {
        return babPcsDetailHistoryDAO.delete(pojo);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabPcsDetailHistoryDAO;
import com.advantech.model.BabPcsDetailHistory;
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
public class BabPcsDetailHistoryService {

    @Autowired
    private BabPcsDetailHistoryDAO babPcsDetailHistoryDAO;

    public List<BabPcsDetailHistory> findByBab(int bab_id) {
        return babPcsDetailHistoryDAO.findByBab(bab_id);
    }

    public List<Map> findByBabForMap(int bab_id) {
        return babPcsDetailHistoryDAO.findByBabForMap(bab_id);
    }

}

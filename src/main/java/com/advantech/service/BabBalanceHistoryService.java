/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabBalanceHistoryDAO;
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
public class BabBalanceHistoryService {

    @Autowired
    private BabBalanceHistoryDAO babBalanceHistoryDAO;

    public List<Map> findByBab(int bab_id) {
        return babBalanceHistoryDAO.findByBab(bab_id);
    }

}

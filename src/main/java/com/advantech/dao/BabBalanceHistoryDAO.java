/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BabBalanceHistory;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabBalanceHistoryDAO extends AbstractDao<Integer, BabBalanceHistory> {

    public List<Map> findByBab(int bab_id) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("bab.id", bab_id))
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }
}

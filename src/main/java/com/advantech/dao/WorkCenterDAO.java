/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.WorkCenter;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorkCenterDAO extends BasicDAOImpl<Integer, WorkCenter> {

    public List<WorkCenter> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }
    
    public List<WorkCenter> findByBusinessGroup(int businessGroupId) {
        return super.createEntityCriteria()
                .createAlias("businessGroup", "b")
                .add(Restrictions.eq("b.id", businessGroupId))
                .list();
    }

    
}

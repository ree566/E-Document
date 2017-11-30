/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.ActionCodeMapping;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ActionCodeMappingDAO extends AbstractDao<Integer, ActionCodeMapping> implements BasicDAO_1<ActionCodeMapping> {

    @Override
    public List<ActionCodeMapping> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ActionCodeMapping findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<ActionCodeMapping> findByActionCode(int ac_id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("actionCode.id", ac_id));
        return c.list();
    }

    @Override
    public int insert(ActionCodeMapping pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(ActionCodeMapping pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ActionCodeMapping pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

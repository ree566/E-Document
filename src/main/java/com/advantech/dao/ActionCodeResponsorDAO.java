/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.ActionCodeResponsor;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ActionCodeResponsorDAO extends AbstractDao<Integer, ActionCodeResponsor> implements BasicDAO_1<ActionCodeResponsor> {

    @Override
    public List<ActionCodeResponsor> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ActionCodeResponsor findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<ActionCodeResponsor> findByActionCode(int ac_id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("actionCode.id", ac_id));
        return c.list();
    }

    @Override
    public int insert(ActionCodeResponsor pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(ActionCodeResponsor pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ActionCodeResponsor pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

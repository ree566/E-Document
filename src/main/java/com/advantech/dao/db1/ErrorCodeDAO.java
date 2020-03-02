/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.ErrorCode;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ErrorCodeDAO extends AbstractDao_1<Integer, ErrorCode> implements BasicDAO_1<ErrorCode> {

    @Override
    public List<ErrorCode> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ErrorCode findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public List<ErrorCode> findByPrimaryKeys(Integer... obj_ids) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.in("id", obj_ids));
        return c.list();
    }

    @Override
    public int insert(ErrorCode pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(ErrorCode pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ErrorCode pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

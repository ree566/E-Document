/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserProfile;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserProfileDAO extends AbstractDao<Integer, UserProfile> implements BasicDAO<UserProfile> {

    @Override
    public List<UserProfile> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public UserProfile findByPrimaryKey(Object obj_id) {
        return getByKey((int) obj_id);
    }

    public List<UserProfile> findByPrimaryKeys(Integer... ids) {
        Criteria c = createEntityCriteria();
        c.add(Restrictions.in("id", ids));
        return c.list();
    }

    public UserProfile findByType(String typeName) {
        Criteria c = createEntityCriteria();
        c.add(Restrictions.eq("type", typeName));
        return (UserProfile) c.uniqueResult();
    }

    @Override
    public int insert(UserProfile pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(UserProfile pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(UserProfile pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

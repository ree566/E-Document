/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserNotification;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserNotificationDAO extends AbstractDao<Integer, UserNotification> implements BasicDAO<UserNotification> {

    @Override
    public List<UserNotification> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public UserNotification findByPrimaryKey(Object obj_id) {
        return getByKey((int) obj_id);
    }

    public UserNotification findByName(String name) {
        Criteria c = createEntityCriteria();
        c.add(Restrictions.eq("name", name));
        return (UserNotification) c.uniqueResult();
    }

    @Override
    public int insert(UserNotification pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(UserNotification pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(UserNotification pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

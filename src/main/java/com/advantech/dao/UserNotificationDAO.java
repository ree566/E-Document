/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserNotification;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserNotificationDAO extends BasicDAOImpl<Integer, UserNotification> {

    public UserNotification findByName(String name) {
        Criteria c = createEntityCriteria();
        c.add(Restrictions.eq("name", name));
        return (UserNotification) c.uniqueResult();
    }

}

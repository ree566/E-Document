/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserProfileDAO extends BasicDAOImpl<Integer, UserProfile> {

    public UserProfile findByType(String typeName) {
        Criteria c = createEntityCriteria();
        c.add(Restrictions.eq("type", typeName));
        return (UserProfile) c.uniqueResult();
    }

}

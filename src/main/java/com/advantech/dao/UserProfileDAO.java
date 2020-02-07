/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserProfile;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserProfileDAO extends AbstractDao<Integer, UserProfile> implements BasicDAO_1<UserProfile> {

    @Override
    public List<UserProfile> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public UserProfile findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
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

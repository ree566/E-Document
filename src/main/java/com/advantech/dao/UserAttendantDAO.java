/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.UserAttendant;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserAttendantDAO extends AbstractDao<Integer, UserAttendant> implements BasicDAO_1<UserAttendant> {

    @Override
    public List<UserAttendant> findAll() {
        return super.createEntityCriteria().list();
    }

    public List<UserAttendant> findByDate(DateTime d) {
        DateTime sD = new DateTime(d).withTime(0, 0, 0, 0);
        DateTime eD = new DateTime(d).withTime(23, 0, 0, 0);
        return super.createEntityCriteria()
                .add(Restrictions.between("createDate", sD.toDate(), eD.toDate()))
                .list();
    }

    @Override
    public UserAttendant findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(UserAttendant pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(UserAttendant pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(UserAttendant pojo) {
        this.getSession().delete(pojo);
        return 1;
    }
}

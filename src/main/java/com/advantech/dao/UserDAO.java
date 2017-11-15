/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserDAO extends AbstractDao<Integer, User> implements BasicDAO_1<User> {

    @Override
    public List<User> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public User findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public User findByJobnumber(String jobnumber) {
        Session session = super.getSession();
        Query q = session.createSQLQuery("SELECT * FROM identitView WHERE jobnumber = :jobnumber");
        q.setParameter("jobnumber", jobnumber);
        q.setResultTransformer(Transformers.aliasToBean(User.class));
        return (User) q.uniqueResult();
    }

    public List<User> findLineOwner(int line_id) {
        return getSession()
                .createQuery("select u from User u left join u.lines line where line.id = :line_id")
                .setParameter("line_id", line_id)
                .list();
    }
    
    public List<User> findLineOwnerBySitefloor(int floor_id){
        return getSession()
                .createQuery("select distinct u from User u left join u.lines line where line.floor.id = :floor_id")
                .setParameter("floor_id", floor_id)
                .list();
    }

    public List<User> findByUserNotification(String notification_name) {
        return getSession()
                .createQuery("select u from User u join u.userNotifications noti where noti.name = :notification_name")
                .setParameter("notification_name", notification_name)
                .list();
    }
    
    public List<User> findByUserNotificationAndNotLineOwner(String notification_name) {
        return getSession()
                .createQuery("select u from User u join u.userNotifications noti "
                        + "where noti.name = :notification_name "
                        + "and u.lines IS EMPTY")
                .setParameter("notification_id", notification_name)
                .list();
    }
    
    public List<User> findByUserNotificationAndNotLineOwner(int floor_id, String notification_name) {
        return getSession()
                .createQuery("select u from User u join u.userNotifications noti "
                        + "where noti.name = :notification_name "
                        + "and floor.id = floor_id "
                        + "and u.lines IS EMPTY")
                .setParameter("notification_id", notification_name)
                .list();
    }

    @Override
    public int insert(User pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(User pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(User pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

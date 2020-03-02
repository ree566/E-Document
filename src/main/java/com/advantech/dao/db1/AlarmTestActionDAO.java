/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.AlarmTestAction;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class AlarmTestActionDAO extends AbstractDao_1<String, AlarmTestAction> implements BasicDAO_1<AlarmTestAction> {

    @Override
    public List<AlarmTestAction> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public AlarmTestAction findByPrimaryKey(Object obj_id) {
        return super.getByKey((String) obj_id);
    }

    @Override
    public int insert(AlarmTestAction pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<AlarmTestAction> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (AlarmTestAction a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int update(AlarmTestAction pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    public int update(List<AlarmTestAction> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (AlarmTestAction a : l) {
            session.update(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int delete(AlarmTestAction pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

    public int delete(List<AlarmTestAction> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (AlarmTestAction a : l) {
            session.delete(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

}

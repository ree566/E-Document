/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.WorktimeAutouploadSetting;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeAutouploadSettingDAO extends AbstractDao<Integer, WorktimeAutouploadSetting> implements BasicDAO<WorktimeAutouploadSetting> {

    @Override
    public List<WorktimeAutouploadSetting> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public WorktimeAutouploadSetting findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<WorktimeAutouploadSetting> findByPrimaryKeys(Integer... id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.in("id", id));
        return c.list();
    }

    @Override
    public int insert(WorktimeAutouploadSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(WorktimeAutouploadSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(WorktimeAutouploadSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

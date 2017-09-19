/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.WorktimeFormulaSetting;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeFormulaSettingDAO extends AbstractDao<Integer, WorktimeFormulaSetting> implements BasicDAO<WorktimeFormulaSetting> {

    @Override
    public List<WorktimeFormulaSetting> findAll() {
        return createEntityCriteria().list();
    }

    @Override
    public WorktimeFormulaSetting findByPrimaryKey(Object obj_id) {
        return getByKey((int) obj_id);
    }

    public List<WorktimeFormulaSetting> findByWorktime(int worktimeId) {
        Criteria c = createEntityCriteria();
        c.add(Restrictions.eq("worktime.id", worktimeId));
        return c.list();
    }

    public List<WorktimeFormulaSetting> findWithWorktime() {
        Criteria c = createEntityCriteria();
        c.setFetchMode("worktime", FetchMode.JOIN);
        return c.list();
    }

    @Override
    public int insert(WorktimeFormulaSetting pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(WorktimeFormulaSetting pojo) {
        getSession().update(pojo);
        return 1;
    }

    public int merge(WorktimeFormulaSetting pojo) {
        getSession().merge(pojo);
        return 1;
    }

    @Override
    public int delete(WorktimeFormulaSetting pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

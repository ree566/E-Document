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
public class WorktimeFormulaSettingDAO extends BasicDAOImpl<Integer, WorktimeFormulaSetting> {

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

    public int merge(WorktimeFormulaSetting pojo) {
        getSession().merge(pojo);
        return 1;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.PrepareScheduleEndtimeSetting;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PrepareScheduleEndtimeSettingDAO extends AbstractDao<String, PrepareScheduleEndtimeSetting> implements BasicDAO_1<PrepareScheduleEndtimeSetting> {

    @Override
    public List<PrepareScheduleEndtimeSetting> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PrepareScheduleEndtimeSetting findByPrimaryKey(Object obj_id) {
        return super.getByKey((String) obj_id);
    }

    @Override
    public int insert(PrepareScheduleEndtimeSetting pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PrepareScheduleEndtimeSetting pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PrepareScheduleEndtimeSetting pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}

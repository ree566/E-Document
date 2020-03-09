/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.Fqc;
import com.advantech.model.db1.FqcSettingHistory;
import java.util.List;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcSettingHistoryDAO extends AbstractDao<Integer, FqcSettingHistory> implements BasicDAO_1<FqcSettingHistory> {

    @Override
    public List<FqcSettingHistory> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public FqcSettingHistory findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<FqcSettingHistory> findProcessing() {
        return super.createEntityCriteria()
                .add(Restrictions.gt("beginTime", new DateTime().withHourOfDay(0).toDate()))
                .add(Restrictions.isNull("lastUpdateTime"))
                .setFetchMode("fqc", FetchMode.JOIN)
                .setFetchMode("fqc.fqcLine", FetchMode.JOIN)
                .list();
    }

    public List<FqcSettingHistory> findProcessing(int fqcLine_id) {
        return super.createEntityCriteria()
                .add(Restrictions.gt("beginTime", new DateTime().withHourOfDay(0).toDate()))
                .add(Restrictions.isNull("lastUpdateTime"))
                .createAlias("fqc", "f")
                .add(Restrictions.eq("f.fqcLine.id", fqcLine_id))
                .list();
    }

    public FqcSettingHistory findByFqc(Fqc fqc) {
        return (FqcSettingHistory) super.createEntityCriteria()
                .add(Restrictions.eq("fqc.id", fqc.getId()))
                .uniqueResult();
    }
    
    public List<FqcSettingHistory> findByFqcIdIn(Integer... ids){
        return super.createEntityCriteria()
                .add(Restrictions.in("fqc.id", ids))
                .list();
    }

    @Override
    public int insert(FqcSettingHistory pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FqcSettingHistory pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FqcSettingHistory pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}

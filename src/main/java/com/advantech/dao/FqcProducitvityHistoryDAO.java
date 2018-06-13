/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BabStatus;
import com.advantech.model.FqcProducitvityHistory;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcProducitvityHistoryDAO extends AbstractDao<Integer, FqcProducitvityHistory> implements BasicDAO_1<FqcProducitvityHistory> {

    @Override
    public List<FqcProducitvityHistory> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public FqcProducitvityHistory findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<Map> findComplete(DateTime sD, DateTime eD) {
        DateTime now = new DateTime();
        Criteria c = super.createEntityCriteria()
                .createAlias("fqc", "fqc")
                .createAlias("fqc.fqcLine", "fqcLine")
                .createAlias("fqc.fqcSettingHistorys", "fqcSettingHistorys")
                .add(Restrictions.eq("fqc.babStatus", BabStatus.CLOSED))
                .add(Restrictions.isNotNull("fqc.lastUpdateTime"))
                .setProjection(Projections.projectionList()
                        .add(Projections.property("fqc.id"), "id")
                        .add(Projections.property("fqcLine.name"), "fqcLineName")
                        .add(Projections.property("fqc.po"), "po")
                        .add(Projections.property("fqc.modelName"), "modelName")
                        .add(Projections.property("standardTime"), "standardTime")
                        .add(Projections.property("pcs"), "pcs")
                        .add(Projections.property("standardTime"), "standardTime")
                        .add(Projections.property("timeCost"), "timeCost")
                        .add(Projections.property("fqc.beginTime"), "beginTime")
                        .add(Projections.property("fqc.lastUpdateTime"), "lastUpdateTime")
                        .add(Projections.property("fqcSettingHistorys.jobnumber"), "jobnumber")
                        .add(Projections.property("fqc.remark"), "remark")
                )
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        if (sD != null && eD != null) {
            c.add(Restrictions.between("fqc.beginTime", sD.withHourOfDay(0).toDate(), eD.withHourOfDay(23).toDate()));
        } else {
            c.add(Restrictions.between("fqc.beginTime", now.withHourOfDay(0).toDate(), now.withHourOfDay(23).toDate()));
        }

        return c.list();
    }
    
    public FqcProducitvityHistory findByFqc(int fqc_id){
        return (FqcProducitvityHistory) super.createEntityCriteria()
                .add(Restrictions.eq("fqc.id", fqc_id))
                .uniqueResult();
    }

    @Override
    public int insert(FqcProducitvityHistory pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FqcProducitvityHistory pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FqcProducitvityHistory pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Bab;
import com.advantech.helper.PropertiesReader;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
@Repository
public class BabDAO extends AbstractDao<Integer, Bab> implements BasicDAO_1<Bab> {

    @PostConstruct
    public void BABDAO() {

    }

    @Override
    public List<Bab> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public Bab findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public Bab findWithLineInfo(int bab_id) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("id", bab_id));
        c.createAlias("line", "l");
        c.createAlias("l.lineType", "lt");
        return (Bab) c.uniqueResult();
    }

    public List<Bab> findByModelAndDate(String modelName, DateTime sD, DateTime eD) {
        sD = sD.withHourOfDay(0);
        eD = eD.withHourOfDay(23);
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("modelName", modelName));
        c.add(Restrictions.between("beginTime", sD.toDate(), eD.toDate()));
        c.setFetchMode("line", FetchMode.JOIN);
        return c.list();
    }

    public List<Bab> findByDate(DateTime sD, DateTime eD) {
        sD = sD.withHourOfDay(0);
        eD = eD.withHourOfDay(23);
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.between("beginTime", sD.toDate(), eD.toDate()));
        return c.list();
    }

    public List<Bab> findByMultipleClause(DateTime sD, DateTime eD, int lineType_id, int floor_id, boolean isAboveTenPcs) {
        sD = sD.withHourOfDay(0);
        eD = eD.withHourOfDay(23);
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.between("beginTime", sD.toDate(), eD.toDate()));
        c.createAlias("line", "l");
        if (lineType_id != -1) {
            c.add(Restrictions.eq("l.lineType.id", lineType_id));
        }
        if (floor_id != -1) {
            c.add(Restrictions.eq("l.floor.id", floor_id));
        }
        if (isAboveTenPcs) {
            c.createAlias("babAlarmHistorys", "bah");
            c.add(Restrictions.gt("bah.totalPcs", 10 - 1));
        }
        return c.list();
    }

    public List<Bab> findProcessing() {
        DateTime d = new DateTime();
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.isNull("babStatus"));
        c.add(Restrictions.between("beginTime", d.withHourOfDay(0).toDate(), d.withHourOfDay(23).toDate()));
        c.createAlias("line", "l");
        c.createAlias("l.lineType", "lineType");
        return c.list();
    }

    public List<Bab> findProcessingByLine(int line_id) {
        DateTime d = new DateTime();
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("line.id", line_id));
        c.add(Restrictions.isNull("babStatus"));
        c.add(Restrictions.between("beginTime", d.withHourOfDay(0).toDate(), d.withHourOfDay(23).toDate()));
        c.addOrder(Order.asc("id"));
        return c.list();
    }

    public List<String> findAllModelName() {
        Criteria c = super.createEntityCriteria();
        c.setProjection(Projections.distinct(Projections.property("modelName")));
        return c.list();
    }

    @Override
    public int insert(Bab pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Bab pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    public int closeBabDirectly(Bab b) {
        super.getSession()
                .createSQLQuery("{CALL usp_CloseBabDirectly(:bab_id)}")
                .setParameter("bab_id", b.getId())
                .executeUpdate();
        return 1;
    }

    public int closeBabWithSaving(Bab b) {
        super.getSession()
                .createSQLQuery("{CALL usp_CloseBabWithSaving(:bab_id)}")
                .setParameter("bab_id", b.getId())
                .executeUpdate();
        return 1;
    }

    @Override
    public int delete(Bab pojo) {
        this.getSession().delete(pojo);
        return 1;
    }
}

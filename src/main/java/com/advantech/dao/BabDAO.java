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

    private boolean saveToOldDB;

    @PostConstruct
    public void BABDAO() {
        PropertiesReader p = PropertiesReader.getInstance();
        saveToOldDB = p.isSaveToOldDB();
    }

    @Override
    public List<Bab> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public Bab findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<Bab> findByModelAndDate(String modelName, DateTime sD, DateTime eD) {
        sD = sD.withHourOfDay(0);
        eD = eD.withHourOfDay(23);
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("modelName", modelName));
        c.add(Restrictions.between("beginTime", sD.toDate(), eD.toDate()));
        return c.list();
    }

    public List<Bab> findByDate(DateTime sD, DateTime eD) {
        sD = sD.withHourOfDay(0);
        eD = eD.withHourOfDay(23);
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.between("beginTime", sD.toDate(), eD.toDate()));
        return c.list();
    }

    public List<Bab> findProcessing() {
        DateTime d = new DateTime();
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.isNull("isused"));
        c.add(Restrictions.between("beginTime", d.withHourOfDay(0).toDate(), d.withHourOfDay(23).toDate()));
        return c.list();
    }

    public List<Bab> findProcessingByLine(int line_id) {
        DateTime d = new DateTime();
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("line.id", line_id));
        c.add(Restrictions.isNull("isused"));
        c.add(Restrictions.between("beginTime", d.withHourOfDay(0).toDate(), d.withHourOfDay(23).toDate()));
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

    @Override
    public int delete(Bab pojo) {
        this.getSession().delete(pojo);
        return 1;
    }
}

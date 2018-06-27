/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.BabStatus;
import com.advantech.model.Fqc;
import java.util.List;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FqcDAO extends AbstractDao<Integer, Fqc> implements BasicDAO_1<Fqc> {

    @Override
    public List<Fqc> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public Fqc findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<Fqc> findProcessing() {
        return super.createEntityCriteria()
                .add(Restrictions.or(
                        Restrictions.isNull("babStatus"),
                        Restrictions.eq("babStatus", BabStatus.PAUSE)))
                .add(Restrictions.isNull("lastUpdateTime"))
                .list();
    }

    public List<Fqc> findProcessing(int fqcLine_id) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("fqcLine.id", fqcLine_id))
                .add(Restrictions.or(
                        Restrictions.isNull("babStatus"),
                        Restrictions.eq("babStatus", BabStatus.PAUSE)))
                .add(Restrictions.isNull("lastUpdateTime"))
                .list();
    }

    public List<Fqc> findProcessingWithLine() {
        return super.createEntityCriteria()
                .setFetchMode("fqcLine", FetchMode.JOIN)
                .add(Restrictions.or(
                        Restrictions.isNull("babStatus"),
                        Restrictions.eq("babStatus", BabStatus.PAUSE)))
                .add(Restrictions.isNull("lastUpdateTime"))
                .list();
    }

    public List<Fqc> findByLineAndPo(int fqcLine_id, String po) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("fqcLine.id", fqcLine_id))
                .add(Restrictions.eq("po", po))
                .list();
    }

    public List<Fqc> findReconnectable(int fqcLine_id, String po) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("fqcLine.id", fqcLine_id))
                .add(Restrictions.eq("po", po))
                .add(Restrictions.eq("babStatus", BabStatus.UNFINSHED_RECONNECTABLE))
                .list();
    }

    @Override
    public int insert(Fqc pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(Fqc pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Fqc pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}

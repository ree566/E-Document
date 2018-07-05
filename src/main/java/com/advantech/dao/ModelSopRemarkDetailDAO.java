/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.ModelSopRemarkDetail;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class ModelSopRemarkDetailDAO extends AbstractDao<Integer, ModelSopRemarkDetail> implements BasicDAO_1<ModelSopRemarkDetail> {

    @Override
    public List<ModelSopRemarkDetail> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public ModelSopRemarkDetail findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<ModelSopRemarkDetail> findByModelAndPeopleAndStation(String modelName, int people, int station) {
        //Model's sop may be multiple.

        return super.getSession().createQuery(
                "select d1 from ModelSopRemarkDetail d1 join fetch d1.modelSopRemark r1 where r1.id = "
                + "(select r2.id from ModelSopRemarkDetail d2 join d2.modelSopRemark r2 "
                + "where r2.modelName = :modelName group by r2 having count(1) = (cast (:people as long))) "
                + "and d1.station = :station")
                .setParameter("modelName", modelName)
                .setParameter("people", people)
                .setParameter("station", station)
                .list();
    }

    @Override
    public int insert(ModelSopRemarkDetail pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(ModelSopRemarkDetail pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(ModelSopRemarkDetail pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}

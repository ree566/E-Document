/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db3;

import com.advantech.model.db1.Worktime;
import java.util.List;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository(value = "sqlViewDAO3")
public class SqlViewDAO extends AbstractDao<Integer, Object> {

    public List<Worktime> findWorktime() {
        return super.getSession()
                .createSQLQuery("select modelName, floorName, speOwnerName, eeOwnerName, qcOwnerName,"
                        + "assyTime assy, t1Time t1, t2Time t2, t3Time t3, t4Time t4,"
                        + "packingTime packing, preAssyTime preAssy, assyPeople, packingPeople "
                        + "from vw_WorkTime")
                .setResultTransformer(Transformers.aliasToBean(Worktime.class))
                .list();
    }

}

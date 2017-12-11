/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.view.BabAvg;
import com.advantech.model.view.BabLastGroupStatus;
import com.advantech.model.view.SensorCurrentGroupStatus;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.model.view.Worktime;
import java.util.List;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class SqlViewDAO extends AbstractDao<Integer, Object> {

    public List<BabAvg> findBabAvg(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabAvg(:bab_id)")
                .addScalar("bab_id", StandardBasicTypes.INTEGER)
                .addScalar("station", StandardBasicTypes.INTEGER)
                .addScalar("average", StandardBasicTypes.DOUBLE)
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabAvg.class))
                .list();
    }

    public Worktime findWorktimeByModelName(String modelName) {
        return (Worktime) super.getSession()
                .createSQLQuery("select * from vw_worktime where modelName = :modelName")
                .setParameter("modelName", modelName)
                .setResultTransformer(Transformers.aliasToBean(Worktime.class))
                .uniqueResult();
    }

    public UserInfoRemote findUserInfoRemote(String jobnumber) {
        return (UserInfoRemote) super.getSession()
                .createSQLQuery("select * from vw_UserInfoRemote where jobnumber = :jobnumber")
                .setParameter("jobnumber", jobnumber)
                .setResultTransformer(Transformers.aliasToBean(UserInfoRemote.class))
                .uniqueResult();
    }

    public List<BabLastGroupStatus> findBabLastGroupStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLastGroupStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabLastGroupStatus.class))
                .list();
    }

    public List<SensorCurrentGroupStatus> findSensorCurrentGroupStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetSensorCurrentGroupStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(SensorCurrentGroupStatus.class))
                .list();
    }
}

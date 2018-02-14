/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Fbn;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FbnDAO extends AbstractDao<Integer, Fbn> {

    public List<Fbn> findToday() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("YY/MM/dd");
        return super.createEntityCriteria()
                .add(Restrictions.eq("LogDate", dtf.print(new DateTime())))
                .list();
    }

    public Fbn getLastInputData() {
        DetachedCriteria maxId = DetachedCriteria.forClass(Fbn.class)
                .setProjection(Projections.max("id"));
        return (Fbn) super.createEntityCriteria()
                .add(Property.forName("id").eq(maxId))
                .uniqueResult();
    }

    //利用檢視表(過濾後FBN資料表資訊)得到當前sensor時間 websocket用 
    public List<Map> getSensorCurrentStatus() {
        return super.getSession()
                .createSQLQuery("SELECT * FROM vw_SensorStatusPerStationToday")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> getTotalAbnormalData(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL sensorTotalAbnormalCheck(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> getAbnormalData(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL sensorAbnormalCheck(?)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public int sensorDataClean(String date) {
        super.getSession()
                .createSQLQuery("{CALL usp_DeleteSensorData(:date)}")
                .setParameter("date", date)
                .executeUpdate();
        return 1;
    }

}

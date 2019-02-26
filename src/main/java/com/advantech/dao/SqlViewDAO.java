/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.view.BabAvg;
import com.advantech.model.view.BabLastBarcodeStatus;
import com.advantech.model.view.BabLastGroupStatus;
import com.advantech.model.view.SensorCurrentGroupStatus;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.model.view.Worktime;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;
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

    public List<BabAvg> findBabAvgWithBarcode(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabAvg_WithBarcode(:bab_id)")
                .addScalar("bab_id", StandardBasicTypes.INTEGER)
                .addScalar("station", StandardBasicTypes.INTEGER)
                .addScalar("average", StandardBasicTypes.DOUBLE)
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabAvg.class))
                .list();
    }

    public List<BabAvg> findBabAvgInHistory(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabAvg_history(:bab_id)")
                .addScalar("bab_id", StandardBasicTypes.INTEGER)
                .addScalar("station", StandardBasicTypes.INTEGER)
                .addScalar("average", StandardBasicTypes.DOUBLE)
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabAvg.class))
                .list();
    }

    public List<Worktime> findWorktime() {
        return super.getSession()
                .createCriteria(Worktime.class)
                .list();
    }

    public Worktime findWorktime(String modelName) {
        return (Worktime) super.getSession()
                .createCriteria(Worktime.class)
                .add(Restrictions.eq("modelName", modelName))
                .uniqueResult();
    }

    public UserInfoRemote findUserInfoRemote(String jobnumber) {
        return (UserInfoRemote) super.getSession()
                .createCriteria(UserInfoRemote.class)
                .add(Restrictions.eq("jobnumber", jobnumber))
                .uniqueResult();
    }

    public List<BabLastGroupStatus> findBabLastGroupStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLastGroupStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabLastGroupStatus.class))
                .list();
    }

    public List<BabLastBarcodeStatus> findBabLastBarcodeStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLastBarcodeStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabLastBarcodeStatus.class))
                .list();
    }

    //For job "CheckSensor.java" check sensor
    public List<SensorCurrentGroupStatus> findSensorCurrentGroupStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetSensorCurrentGroupStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(SensorCurrentGroupStatus.class))
                .list();
    }

    public List<Map> findSensorStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_GetSensorStatus(:bab_id)")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBarcodeStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_GetBarcodeStatus(:bab_id)")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBalanceDetail(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabBalanceDetail(:bab_id)")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBalanceDetailWithBarcode(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabBalanceDetailWithBarcode(:bab_id)")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Join detail with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findBabDetail(int lineType_id, int floor_id, DateTime sD, DateTime eD, boolean isAboveStandard) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetBabDetail_1(:lineType_id, :floor_id, :sD, :eD, :minPcs)}")
                .setParameter("lineType_id", lineType_id)
                .setParameter("floor_id", floor_id)
                .setParameter("sD", sD.withHourOfDay(0).toDate())
                .setParameter("eD", eD.withHourOfDay(23).toDate())
                .setParameter("minPcs", isAboveStandard ? 10 : "-1")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Join detail with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findBabDetailWithBarcode(int lineType_id, int floor_id, DateTime sD, DateTime eD, boolean isAboveStandard) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetBabDetail_WithBarcode(:lineType_id, :floor_id, :sD, :eD, :minPcs)}")
                .setParameter("lineType_id", lineType_id)
                .setParameter("floor_id", floor_id)
                .setParameter("sD", sD.withHourOfDay(0).toDate())
                .setParameter("eD", eD.withHourOfDay(23).toDate())
                .setParameter("minPcs", isAboveStandard ? 10 : "-1")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Get bananceCompare with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findLineBalanceCompareByBab(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLineBalanceCompareByBab(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Get bananceCompare with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findLineBalanceCompareByBabWithBarcode(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLineBalanceCompareByBab_WithBarcode(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Get bananceCompare with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findLineBalanceCompare(String modelName, String lineTypeName) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLineBalanceCompare(:modelName, :lineTypeName)}")
                .setParameter("modelName", modelName)
                .setParameter("lineTypeName", lineTypeName)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findSensorStatusPerStationToday() {
        return super.getSession()
                .createSQLQuery("select * from vw_SensorStatusPerStationToday")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabPcsDetail(String modelName, String lineType, DateTime startDate, DateTime endDate) {

        if (startDate != null && endDate != null) {
            startDate = startDate.withHourOfDay(0);
            endDate = endDate.withHourOfDay(23);
        }

        return super.getSession()
                .createSQLQuery("{CALL usp_Excel_PcsDetail(:modelName, :lineType, :startDate, :endDate)}")
                .setParameter("modelName", modelName)
                .setParameter("lineType", "-1".equals(lineType) ? null : lineType)
                .setParameter("startDate", startDate != null ? startDate.toDate() : startDate)
                .setParameter("endDate", endDate != null ? endDate.toDate() : endDate)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabPcsDetailWithBarcode(String modelName, String lineType, DateTime startDate, DateTime endDate) {

        if (startDate != null && endDate != null) {
            startDate = startDate.withHourOfDay(0);
            endDate = endDate.withHourOfDay(23);
        }

        return super.getSession()
                .createSQLQuery("{CALL usp_Excel_PcsDetail_WithBarcode(:modelName, :lineType, :startDate, :endDate)}")
                .setParameter("modelName", modelName)
                .setParameter("lineType", "-1".equals(lineType) ? null : lineType)
                .setParameter("startDate", startDate != null ? startDate.toDate() : startDate)
                .setParameter("endDate", endDate != null ? endDate.toDate() : endDate)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabLineProductivity(String po, String modelName, Integer line_id, String jobnumber, Integer minPcs, DateTime sD, DateTime eD) {
        return super.getSession()
                .createSQLQuery("{CALL usp_Excel_LineProductivity(:po, :modelName, :lineId, :jobnumber, :minPcs, :sD, :eD)}")
                .setParameter("po", po)
                .setParameter("modelName", modelName)
                .setParameter("lineId", line_id)
                .setParameter("jobnumber", jobnumber)
                .setParameter("minPcs", minPcs)
                .setParameter("sD", sD.withHourOfDay(0).toDate())
                .setParameter("eD", eD.withHourOfDay(23).toDate())
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabLineProductivityWithBarcode(String po, String modelName, Integer line_id, String jobnumber, Integer minPcs, DateTime sD, DateTime eD) {
        return super.getSession()
                .createSQLQuery("{CALL usp_Excel_LineProductivity_WithBarcode(:po, :modelName, :lineId, :jobnumber, :minPcs, :sD, :eD)}")
                .setParameter("po", po)
                .setParameter("modelName", modelName)
                .setParameter("lineId", line_id)
                .setParameter("jobnumber", jobnumber)
                .setParameter("minPcs", minPcs)
                .setParameter("sD", sD.withHourOfDay(0).toDate())
                .setParameter("eD", eD.withHourOfDay(23).toDate())
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabPassStationRecord(String po, String modelName, DateTime sD, DateTime eD, String lineType) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetBabPassStationRecord_1(:po, :modelName, :sD, :eD, :lineType)}")
                .setParameter("po", po)
                .setParameter("modelName", modelName)
                .setParameter("sD", sD != null ? sD.withHourOfDay(0).toDate() : null)
                .setParameter("eD", eD != null ? eD.withHourOfDay(23).toDate() : null)
                .setParameter("lineType", lineType)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabPassStationExceptionReport(String po, String modelName, DateTime sD, DateTime eD, int lineType_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_BabPassStation_ExceptionReport(:po, :modelName, :sD, :eD, :lineType_id)}")
                .setParameter("po", po)
                .setParameter("modelName", modelName)
                .setParameter("sD", sD != null ? sD.withHourOfDay(0).toDate() : null)
                .setParameter("eD", eD != null ? eD.withHourOfDay(23).toDate() : null)
                .setParameter("lineType_id", lineType_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBabPreAssyProductivity(int lineType_id, int floor_id, DateTime sD, DateTime eD) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetBabPreAssyProductivity(:lineType_id, :floor_id, :sD, :eD)}")
                .setParameter("lineType_id", lineType_id)
                .setParameter("floor_id", floor_id)
                .setParameter("sD", sD != null ? sD.withHourOfDay(0).toDate() : null)
                .setParameter("eD", eD != null ? eD.withHourOfDay(23).toDate() : null)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }
}

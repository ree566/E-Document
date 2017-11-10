/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.AlarmAction;
import com.advantech.model.Bab;
import com.advantech.model.BabHistory;
import com.advantech.model.BabStatus;
import com.advantech.helper.PropertiesReader;
import com.advantech.interfaces.AlarmActions;
import com.advantech.service.LineBalancingService;
import java.sql.Array;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
@Repository
public class BabDAO extends BasicDAO implements AlarmActions {

    private static final Logger log = LoggerFactory.getLogger(BabDAO.class);

    private boolean saveToOldDB;

    @Autowired
    private LineBalancingService lineBalanceService;

    @PostConstruct
    public void BABDAO() {
        PropertiesReader p = PropertiesReader.getInstance();
        saveToOldDB = p.isSaveToOldDB();
    }

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<Bab> queryBABTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Bab.class, sql, params);
    }

    private List<BabHistory> getHistoryTable(String sql, Object... params) {
        return queryForBeanList(getConn(), BabHistory.class, sql, params);
    }

    public List<Bab> getBAB() {
        return queryBABTable("SELECT * FROM LS_BAB");
    }

    public Bab getBAB(int bab_id) {
        List l = queryBABTable("SELECT * FROM LS_BAB WHERE id = ?", bab_id);
        return !l.isEmpty() ? (Bab) l.get(0) : null;
    }

    public List<Bab> getTodayBAB() {
        return queryBABTable("SELECT * FROM babWithLineView WHERE btime > DATEADD(DAY, DATEDIFF(DAY, 0, GETDATE()), 0) ORDER BY id");
    }

    public List<Map> getBABForMap() {
        return queryForMapList(getConn(), "SELECT * FROM closedBABView");
    }

    public List<Map> getBABForMap(int bab_id) {
        return queryForMapList(getConn(), "SELECT * FROM closedBABView WHERE id = ?", bab_id);
    }

    public List<Map> getBABForMap(String date) {
        return queryForMapList(getConn(), "SELECT * FROM closedBABView WHERE CONVERT(varchar(10),btime,20) = ? ORDER BY ID", date);
    }

    public List<Bab> getBAB(String modelName, String dateFrom, String dateTo) {
        return queryProcForBeanList(getConn(), Bab.class, "{CALL getBABInTime(?,?,?)}", modelName, dateFrom, dateTo);
    }

    //Quartz用，獲得需要計算ouput的工單
    public List<Bab> getAllProcessing() {
        return queryBABTable("SELECT * FROM LS_BAB_Id_List");
    }

    public List<Bab> getProcessing(int id) {
        return queryBABTable("SELECT * FROM LS_BAB_Id_List WHERE id = ?", id);
    }

    public List<Bab> getAssyProcessing() {
        return queryBABTable("SELECT * FROM assyProcessing");
    }

    public List<BabHistory> getBABHistory(Bab bab) {
        return getHistoryTable("SELECT * FROM LS_BAB_History WHERE BABid = ?", bab.getId());
    }

    public List<Map> getBABInfo(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL getBABDetail_1(?,?)}", startDate, endDate);
    }

    public List<Bab> getProcessingBAB() {
        return queryBABTable("SELECT * FROM LS_BAB_Sort");
    }

    public Bab getProcessingBAB(int bab_id) {
        List l = queryBABTable("SELECT * FROM LS_BAB_Sort WHERE id = ?", bab_id);
        return !l.isEmpty() ? (Bab) l.get(0) : null;
    }

    public List<Bab> getProcessingBABByLine(int lineNo) {
        return queryBABTable("SELECT * FROM LS_BAB_Sort WHERE line = ? ORDER BY id", lineNo);
    }

    public List<Bab> getProcessingBABByPOAndLine(String PO, int line) {
        return queryBABTable("SELECT * FROM LS_BAB_Sort WHERE PO = ? AND line = ?", PO, line);
    }

    public List<Bab> getTimeOutBAB() {
        return queryBABTable("SELECT * FROM LS_BABTimeOutView");//get the timeout bab where hour diff = 2
    }

    public Bab getLastInputBAB(int lineNo) {
        List list = queryBABTable("SELECT TOP 1 * FROM LS_BAB_Sort WHERE line = ? ORDER BY ID DESC", lineNo);
        return list.isEmpty() ? null : (Bab) list.get(0);
    }

    public List<Map> getLastGroupStatus(int bab_id) {
        return queryProcForMapList(getConn(), "{CALL LS_lastGroupStatus(?)}", bab_id);
    }

    public List<Map> getBABAvgs(int bab_id) {
        return queryForMapList(getConn(), "SELECT * FROM BABAVG(?)", bab_id);
    }

    public List<Map> getBABAvgsInSpecGroup(int bab_id, int groupStart, int groupEnd) {
        return queryProcForMapList(getConn(), "{CALL getbabAvgInSpecGroup(?,?,?)}", bab_id, groupStart, groupEnd);
    }

    public List<Map> getClosedBABAVG(int bab_id) throws JSONException {
        return queryForMapList(getConn(), "SELECT * FROM closedBABAVG(?)", bab_id);
    }

    public List<Map> getSensorStatus(int bab_id) {
        return queryForMapList(getConn(), "SELECT * FROM getSensorStatusByBabId(?) order by 1,2", bab_id);
    }

    public List<Map> getBABTimeHistoryDetail(int bab_id) {
        return queryForMapList(getConn(), "SELECT * FROM LS_BABTimeHistoryDetail WHERE BABid = ? ORDER BY TagName, groupid", bab_id);
    }

    public List<Map> getBalancePerGroup(int bab_id) {
        return queryForMapList(getConn(), "SELECT * FROM LS_balanceDetailPerGroup(?)", bab_id);
    }

    public List<Map> getClosedBalanceDetail(int bab_id) {
        return queryForMapList(getConn(), "SELECT * FROM LS_BalanceHistory WHERE BABid = ?", bab_id);
    }

    public List<Map> getLineBalanceCompare(String Model_name, String lineType) {
        return queryForMapList(getConn(), "SELECT * FROM LS_LineBalanceCompare(?,?)", Model_name, lineType);
    }

    public List<Map> getLineBalanceCompare(int BAbid) {
        return queryForMapList(getConn(), "SELECT * FROM LS_LineBalanceCompareById(?)", BAbid);
    }

    public List<Array> getAvailableModelName() {
        return queryForArrayList(getConn(), "SELECT Model_name from LS_availModelName");
    }

    public boolean checkSensorIsClosed(int bab_id, int sensorNo) {
        List historys = getHistoryTable("SELECT * FROM LS_BAB_History WHERE BABid = ? and T_Num = ?", bab_id, sensorNo);
        return !historys.isEmpty();//回傳是否有東西 有true 無 false
    }

    public Integer getPoTotalQuantity(String PO) {
        List<Map> l = queryForMapList(this.getConn(), "SELECT * FROM poQuantityView WHERE PO = ?", PO);
        return l.isEmpty() ? null : (Integer) l.get(0).get("qty");
    }

    public List<Map> getEmptyRecordDownExcel(String startDate, String endDate) {
        return queryProcForMapList(this.getConn(), "{CALL babEmptyRecordDownExcel(?,?)}", startDate, endDate);
    }

    @Override
    public boolean insertAlarm(List<AlarmAction> l) {
        return updateAlarmTable("INSERT INTO Alm_BABAction(alarm, tableId) VALUES(?, ?)", l);
    }

    @Override
    public boolean updateAlarm(List<AlarmAction> l) {
        return updateAlarmTable("UPDATE Alm_BABAction SET alarm = ? WHERE tableId = ?", l);
    }

    @Override
    public boolean resetAlarm() {
        return update(getConn(), "UPDATE Alm_BABAction SET alarm = 0");
    }

    @Override
    public boolean removeAlarmSign() {
        return update(getConn(), "TRUNCATE TABLE Alm_BABAction");
    }

    private boolean updateAlarmTable(String sql, List<AlarmAction> l) {
        return update(getConn(), sql, l, "alarm", "tableId");
    }

    public boolean setBABAlarmToTestingMode() {
        return update(getConn(), "UPDATE Alm_BABAction SET alarm = 1");
    }

    public boolean insertBAB(Bab bab) {
        return update(
                getConn(),
                "INSERT INTO LS_BAB(PO,Model_name,line,people,startPosition,ispre) VALUES (?,?,?,?,?,?)",
                bab.getPO(),
                bab.getModel_name(),
                bab.getLine(),
                bab.getPeople(),
                bab.getStartPosition(),
                bab.getIspre()
        );
    }

    /**
     * Please set the babAvg into bab object if data need to saveAndClose.
     *
     * @param bab
     * @return
     */
    //一連串儲存動作統一commit，不然出問題時會出現A和B資料庫資料不同步問題
    public boolean stopAndSaveBab(Bab bab) {
        this.closeBabWithSaving(bab.getId());
        if (Objects.equals(BabStatus.UNFINSHED.getValue(), bab.getIsused())) {
            this.updateIsused(bab);
        }
        if(saveToOldDB){
            lineBalanceService.insert(bab);
        }
        return true;
    }

    public boolean updateIsused(Bab bab) {
        return update(getConn(), "UPDATE LS_BAB SET isused = ? WHERE id = ?", bab.getIsused(), bab.getId());
    }

    public boolean closeBabWithSaving(int bab_id) {
        return updateProc(getConn(), "{CALL LS_closeBABWithSaving(?)}", bab_id);
    }

    public boolean stopSingleSensor(int sensorId, int bab_id) {
        return updateProc(getConn(), "{CALL LS_Sensor_END(?,?)}", sensorId, bab_id);
    }

    public boolean closeBABDirectly(Bab bab) {
        return updateProc(getConn(), "{call LS_closeBABDirectly(?)}", bab.getId());
    }
}

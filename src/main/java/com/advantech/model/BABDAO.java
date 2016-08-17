/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.AlarmAction;
import com.advantech.helper.ProcRunner;
import com.advantech.helper.PropertiesReader;
import com.advantech.entity.BABHistory;
import com.advantech.entity.BAB;
import com.advantech.entity.LineBalancing;
import com.advantech.service.BasicService;
import com.advantech.service.FBNService;
import com.advantech.service.LineBalanceService;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
public class BABDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(BABDAO.class);

    private final int LIMIT_BAB_DATA;
    private static boolean isSaveToOldDB;

    public BABDAO() {
        LIMIT_BAB_DATA = PropertiesReader.getInstance().getLimitBABData();

        PropertiesReader p = PropertiesReader.getInstance();
        isSaveToOldDB = p.isSaveToOldDB();
    }

    private Connection getConn() {
        return getDBUtilConn(SQL.Way_Chien_WebAccess);
    }

    private List<BAB> queryBABTable(String sql, Object... params) {
        return queryForBeanList(getConn(), BAB.class, sql, params);
    }

    private List<BABHistory> getHistoryTable(String sql, Object... params) {
        return queryForBeanList(getConn(), BABHistory.class, sql, params);
    }

    public List<BAB> getBAB() {
        return queryBABTable("SELECT * FROM LS_BAB");
    }

    public List<BAB> getBAB(int BABid) {
        return queryBABTable("SELECT * FROM LS_BAB WHERE id = ?", BABid);
    }

    public List<BAB> getBAB(String modelName, String dateFrom, String dateTo) {
        return queryProcForBeanList(getConn(), BAB.class, "{CALL getBABInTime(?,?,?)}", modelName, dateFrom, dateTo);
    }

    public List<BAB> getBABIdForCaculate() {
        return queryBABTable("SELECT * FROM LS_BAB_Id_List");
    }

    public List<BABHistory> getBABHistory(BAB bab) {
        return getHistoryTable("SELECT * FROM LS_BAB_History WHERE BABid = ?", bab.getId());
    }

    public List<BAB> getBABInfo(String lineType) {
        return queryProcForBeanList(getConn(), BAB.class, "{CALL getBABByLineType(?)}", lineType);
    }

    public List<BAB> getProcessingBAB() {
        return queryBABTable("SELECT * FROM LS_BAB_Sort");
    }

    public List<BAB> getProcessingBAB(int BABid) {
        return queryBABTable("SELECT * FROM LS_BAB_Sort WHERE id = ?", BABid);
    }

    public List<BAB> getProcessingBABByLine(int lineNo) {
        return queryBABTable("SELECT * FROM LS_BAB_Sort WHERE line = ?", lineNo);
    }

    public List<BAB> getProcessingBABByPOAndLine(String PO, int line) {
        return queryBABTable("SELECT * FROM LS_BAB_Sort WHERE PO = ? AND line = ?", PO, line);
    }

    public List<BAB> getTimeOutBAB() {
        return queryBABTable("SELECT * FROM LS_BABTimeOutView");//get the timeout bab where hour diff = 2
    }

    public BAB getLastInputBAB(int lineNo) {
        List list = queryBABTable("SELECT TOP 1 * FROM LS_BAB_Sort WHERE line = ? ORDER BY ID DESC", lineNo);
        return list.isEmpty() ? null : (BAB) list.get(0);
    }

    public List<Map> getLastGroupStatus(int BABid) {
        return queryProcForMapList(getConn(), "{CALL LS_lastGroupStatus(?)}", BABid);
    }

    public List<Map> getBABAvgs(int BABid) {
        return queryForMapList(getConn(), "SELECT * FROM BABAVG(?)", BABid);
    }

    public List<Map> getBABAvgsInSpecGroup(int BABid) {
        return queryProcForMapList(getConn(), "{CALL getbabAvgInSpecGroup(?)}", BABid);
    }

    public List<Map> getClosedBABAVG(int BABid) throws JSONException {
        return queryForMapList(getConn(), "SELECT * FROM closedBABAVG(?)", BABid);
    }

    public List<Map> getSensorStatus(int BABid) {
        return queryForMapList(getConn(), "SELECT * FROM getSensorStatusByBabId(?) order by 1,2", BABid);
    }

    public List<Map> getBABTimeHistoryDetail(int BABid) {
        return queryForMapList(getConn(), "SELECT * FROM LS_BABTimeHistoryDetail WHERE BABid = ? ORDER BY TagName, groupid", BABid);
    }

    public List<Map> getBalaceDetail(int BABid) {
        return queryForMapList(getConn(), "SELECT * FROM LS_balanceDetailPerGroup(?)", BABid);
    }

    public List<Map> getClosedBalanceDetail(int BABid) {
        return queryForMapList(getConn(), "SELECT * FROM LS_BalanceHistory WHERE BABid = ?", BABid);
    }

    public List<Map> getLineBalanceCompare(String Model_name, String lineType) {
        return queryForMapList(getConn(), "SELECT * FROM LS_LineBalanceCompare(?,?)", Model_name, lineType);
    }

    public List<Map> getLineBalanceCompare(int BAbid) {
        return queryForMapList(getConn(), "SELECT * FROM LS_LineBalanceCompareById(?)", BAbid);
    }

    public List<Map> getClosedBABInfoDetail(String startDate, String endDate) {
        return queryProcForMapList(getConn(), "{CALL closedBABInfoDetail(?,?)}", startDate, endDate);
    }

    public List<Array> getAvailableModelName() {
        return queryForArrayList(getConn(), "SELECT Model_name from LS_availModelName");
    }

    public boolean checkPrevSensorIsClosed(int BABid, int sensorNo) {
        List historys = getHistoryTable("SELECT * FROM LS_BAB_History WHERE BABid = ? and T_Num = ?", BABid, sensorNo);
        return !historys.isEmpty();//回傳是否有東西 有true 無 false
    }

    public boolean updateBABAlarm(List<AlarmAction> l) {
        return updateAlarmTable("UPDATE Alm_BABAction SET alarm = ? WHERE tableId = ?", l);
    }

    public boolean resetBABAlarm() {
        return update(getConn(), "UPDATE Alm_BABAction SET alarm = 0");
    }

    private boolean updateAlarmTable(String sql, List<AlarmAction> l) {
        return update(getConn(), sql, l, "alarm", "tableId");
    }

    public boolean insertBAB(BAB bab) {
        return update(
                getConn(),
                "INSERT INTO LS_BAB(PO,Model_name,line,people) VALUES (?,?,?,?)",
                bab.getPO(),
                bab.getModel_name(),
                bab.getLine(),
                bab.getPeople()
        );
    }

    public boolean recordBABPeople(int BABid, int station, String jobnumber) {
        return update(getConn(), "INSERT INTO BABPeopleRecoard(BABid, station, user_id) VALUES (?,?,?)", BABid, station, jobnumber);
    }

    /**
     * Please set the babAvg into bab object if data need to saveAndClose.
     *
     * @param bab
     * @return
     */
    //一連串儲存動作統一commit，不然出問題時會出現A和B資料庫資料不同步問題
    public boolean stopAndSaveBab(BAB bab) {
        LineBalanceService lineBalanceService = BasicService.getLineBalanceService();
        FBNService fbnService = BasicService.getFbnService();

        boolean flag = false;
        Connection conn1 = null;
        Connection conn2 = null;

        try {
            //Prevent check Babavg data in database if exists or not multiple times, let ouside check and save value into bab object.
            JSONArray balances = bab.getBabavgs();
            if (balances == null) {// check data balance is exist first
                log.error("The babAvg in bab object is not setting value, saving action suspend.");
                return false;
            }
            int dataCount = fbnService.getBalancePerGroup(bab.getId()).size();

            if (dataCount <= LIMIT_BAB_DATA) {
                return closeBABDirectly(bab);
            }

            LineBalancing maxBaln = lineBalanceService.getMaxBalance(bab); //先取得max才insert，不然會抓到自己
            double baln = (double) lineBalanceService.caculateLineBalance(balances);

            QueryRunner qRunner = new QueryRunner();
            ProcRunner pRunner = new ProcRunner();

            //--------區間內請勿再開啟tran不然會deadlock----------------------------
            conn1 = getDBUtilConn(SQL.Way_Chien_WebAccess);
            conn1.setAutoCommit(false);

            if (isSaveToOldDB) {
                conn2 = getDBUtilConn(SQL.Way_Chien_LineBalcing);
                conn2.setAutoCommit(false);
                Double[] data = lineBalanceService.fillBalanceDataToArray(balances);
                Object[] param2 = {
                    bab.getPeople(),
                    bab.getLinetype(),
                    bab.getPO(),
                    bab.getModel_name(),
                    baln,
                    data[0],
                    data[1],
                    data[2],
                    data[3],
                    bab.getLine()
                };
                qRunner.update(conn2,
                        "insert into Line_Balancing_Main(Number_of_poople, Do_not_stop, PO, PN, Balance, "
                        + "Do_not1, Do_not2, Do_not3, Do_not4, Line) values (?,?,?,?,?,?,?,?,?,?)",
                        param2);
                DbUtils.commitAndCloseQuietly(conn2);
            }

            Object[] param3 = {bab.getId()};
            pRunner.updateProc(conn1, "{CALL LS_closeBABWithSaving(?)}", param3);//關閉線別

            //--------區間內請勿再開啟tran不然會deadlock----------------------------
            DbUtils.commitAndCloseQuietly(conn1);
            lineBalanceService.checkLineBalanceAndSendMail(bab, maxBaln, baln);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
            DbUtils.rollbackAndCloseQuietly(conn1);
            if (conn2 != null) {
                DbUtils.rollbackAndCloseQuietly(conn2);
            }
        } catch (MessagingException | JSONException ex) {
            log.error(ex.toString());
            flag = true; //即使寄信失敗一樣傳回true給使用者知道(不需要知道寄信fail log有紀錄即可)
        }
        return flag;
    }

    public boolean stopSingleSensor(int sensorId, int BABid) {
        return updateProc(getConn(), "{CALL LS_Sensor_END(?,?)}", sensorId, BABid);
    }

    public boolean closeBABDirectly(BAB bab) {
        return updateProc(getConn(), "{call LS_closeBABDirectly(?)}", bab.getId());
    }

}

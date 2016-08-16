/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.helper.ProcRunner;
import com.advantech.helper.CronTrigMod;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BasicDAO implements Serializable {

    /*
     change the database driver because of
     http://www.javaworld.com.tw/jute/post/view?bid=6&id=131471&tpg=1&ppg=1&sty=1&age=0
     http://www.javaworld.com.tw/jute/post/view?bid=21&id=366&sty=1&tpg=1&age=-1
        
     old: "jdbc:sqlserver://NB991001\\KEVIN;databaseName=Internal_Check";
     the different is jtds no need to provide the instance name
    
     http://simon-tech.blogspot.tw/2012/02/tomcat-datasource.html
     建議將 JDBC driver 放在 $CATALINA_BASE/lib 路徑下，以免造成 JRE Memory Leak 的問題
     */
    private static final Logger log = LoggerFactory.getLogger(BasicDAO.class);
    private static QueryRunner qRunner;
    private static ProcRunner pRunner;

    private static String DISCONNECT_TRIG_EXP;
    private static String DEFAULT_TRIG_EXP;
    private static String TRIGGER_KEY;
    private static boolean connectFlag = false;
    private static final int RETRY_WAIT_TIME = 3000;

    private static Map<String, DataSource> dataSourceMap;

    public static enum SQL {

        Way_Chien_TWM3("jdbc/res2"),
        Way_Chien_WebAccess("jdbc/res3"),
        Way_Chien_LineBalcing("jdbc/res4");

        SQL(String str) {
            this.str = str;
        }
        private final String str;

        @Override
        public String toString() {
            return str;
        }
    }

    public static void dataSourceInit() {
        qRunner = new QueryRunner();
        pRunner = new ProcRunner();
        dataSourceMap = new HashMap<>();
        DISCONNECT_TRIG_EXP = "0 0/15 8-16 ? * MON-FRI *";
        DEFAULT_TRIG_EXP = "0/30 * 8-17 ? * MON-FRI *";
        TRIGGER_KEY = "DailyJobWorker";

        dataSourceMap.clear();

        try {

            for (SQL sql : SQL.values()) {
                String dataSourceString = sql.toString();
                try {
                    dataSourceMap.put(dataSourceString, getDataSource(dataSourceString));
                } catch (NamingException ex) {
                    log.error(ex.toString());
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private static DataSource getDataSource(String dataSourcePath) throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup(dataSourcePath);
        return dataSource;
    }

    public static Connection getDBUtilConn(SQL sqlType) {
        return openConn(sqlType.toString());
    }

    private synchronized static Connection openConn(String dataSource) {
        Connection conn = null;
        boolean triggerChangeStatus;
        try {
            DataSource source = dataSourceMap.get(dataSource);
            conn = source.getConnection();
            if (connectFlag == true) {
                //當連線正常時 把quartz cron調整回來
                triggerChangeStatus = changeCronTrig(DEFAULT_TRIG_EXP);
                log.info("Database is connected, Update trgger to default: " + triggerChangeStatus);
            }
        } catch (SQLException ex) {
            log.error(ex.toString());
            if (connectFlag == false) {
                //當連線異常時，進入這把排程調整成15分鐘看一下資料庫連線
                triggerChangeStatus = changeCronTrig(DISCONNECT_TRIG_EXP);
                log.error("Database disconnected, Update trigger to disconnected_exp: " + triggerChangeStatus);
            }
        }
        return conn;
    }

    public static List queryForMapList(Connection conn, String sql, Object... params) {
        return query(conn, new MapListHandler(), sql, params);
    }

    public static List queryForBeanList(Connection conn, Class cls, String sql, Object... params) {
        return query(conn, new BeanListHandler(cls), sql, params);
    }

    public static List queryForArrayList(Connection conn, String sql, Object... params) {
        return query(conn, new ArrayListHandler(), sql, params);
    }

    private static List query(Connection conn, ResultSetHandler rsh, String sql, Object... params) {
        List<?> data = null;
        try {
            data = (List) qRunner.query(conn, sql, rsh, params);
        } catch (SQLException e) {
            log.error(e.toString());
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return data == null ? new ArrayList() : data;
    }

    public static boolean update(Connection conn, String sql, List l, String... params) {
        boolean flag = false;
        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(sql);

            for (Object o : l) {
                qRunner.fillStatementWithBean(ps, o, params);
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
            DbUtils.commitAndClose(conn);
            flag = true;
        } catch (SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            log.error(e.toString());
        }
        return flag;
    }

    public static boolean update(Connection conn, String sql, Object... params) {
        boolean flag = false;
        try {
            conn.setAutoCommit(false);
            qRunner.update(conn, sql, params);
            DbUtils.commitAndClose(conn);
            flag = true;
        } catch (SQLException e) {
            // do not retry if we get any other error
            DbUtils.rollbackAndCloseQuietly(conn);
            log.error("Error has occured - Error Code: "
                    + e.getErrorCode() + " SQL STATE :"
                    + e.getSQLState() + " Message : " + e.getMessage());
        }
        return flag;
    }

    public static boolean updateProc(Connection conn, String sql, Object... params) {
        boolean flag = false;
        try {
            pRunner.updateProc(conn, sql, params);
            flag = true;
        } catch (SQLException ex) {
            log.error(ex.toString());
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return flag;
    }

    public static List queryProcForBeanList(Connection conn, Class cls, String sql, Object... params) {
        return queryProc(conn, new BeanListHandler(cls), sql, params);
    }

    public static List queryProcForMapList(Connection conn, String sql, Object... params) {
        return queryProc(conn, new MapListHandler(), sql, params);
    }

    private static List queryProc(Connection conn, ResultSetHandler rsh, String sql, Object... params) {
        List data = null;
        try {
            data = (List) pRunner.queryProc(conn, sql, rsh, params);
        } catch (SQLException e) {
            log.error(e.toString());
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return data == null ? new ArrayList() : data;
    }

    //Change the cron trigger when database is disconnected. 30sec/per --> 15min/per
    private static boolean changeCronTrig(String exp) {
        boolean b = false;
        try {
            CronTrigMod c = CronTrigMod.getInstance();
            b = c.updateCronExpression(TRIGGER_KEY, exp, 0);
            connectFlag = !connectFlag;//有更改時調整flag(開關)做判斷，避免重複修改
        } catch (Exception e) {
            log.error(e.toString());
        }
        return b;
    }

    public static void objectInit() {
        dataSourceMap.clear();
    }
}

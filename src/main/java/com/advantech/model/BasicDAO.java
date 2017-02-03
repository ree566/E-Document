/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.ProcRunner;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
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
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.quartz.SchedulerException;
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

    private static boolean connectFlag = false;
    private static final int RETRY_WAIT_TIME = 3000;

    private static Map<String, DataSource> dataSourceMap;

    public static enum SQL {

        TWM3("jdbc/res2"),
        WebAccess("jdbc/res3"),
        LineBalancing("jdbc/res4");

        SQL(String str) {
            this.str = str;
        }
        private final String str;

        @Override
        public String toString() {
            return str;
        }
    }

    //Set dataSource with JNDI
    public static void dataSourceInit() {
        dataSourceMap = new HashMap<>();
        try {
            for (SQL sql : SQL.values()) {
                String dataSourceString = sql.toString();
                try {
                    DataSource ds = (DataSource) getDataSource(dataSourceString);
                    DataSource pooledDs = DataSources.pooledDataSource(ds);
                    dataSourceMap.put(dataSourceString, pooledDs);
                } catch (NamingException ex) {
                    log.error(ex.toString());
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
        }
    }

    private static DataSource getDataSource(String dataSourcePath) throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup(dataSourcePath);
        return dataSource;
    }

    //Set dataSource without JNDI
    public static void dataSourceInit1() {
        dataSourceMap = new HashMap<>();
        try {
            for (SQL sql : SQL.values()) {
                String dataSourceString = sql.toString();
                try {
                    dataSourceMap.put(dataSourceString, DataSources.pooledDataSource(getDataSource1(dataSourceString)));
                } catch (NamingException ex) {
                    log.error(ex.toString());
                }
            }
        } catch (SQLException e) {
            log.error(e.toString());
        }
    }

    public static DataSource getDataSource1(String dataSourcePath) throws NamingException {
        //中文變亂碼
        JtdsDataSource xaDS = new JtdsDataSource();
        xaDS.setServerName("M3-SERVER");
        xaDS.setDatabaseName(getDbName(dataSourcePath));
        xaDS.setUser("waychien");
        xaDS.setPassword("m3server");
        return xaDS;
    }

    private static String getDbName(String dataSourcePath) {
        if (dataSourcePath.equals(SQL.TWM3.toString())) {
            return "TWM3";
        } else if (dataSourcePath.equals(SQL.WebAccess.toString())) {
            return "WebAccess";
        } else if (dataSourcePath.equals(SQL.LineBalancing.toString())) {
            return "Line_Balancing";
        } else {
            return "";
        }
    }

    public static Connection getDBUtilConn(SQL sqlType) {
        return openConn(sqlType.toString());
    }

    private synchronized static Connection openConn(String dataSource) {
        Connection conn = null;
        boolean triggerChangeStatus;
        try {

            DataSource ds = dataSourceMap.get(dataSource);
            conn = ds.getConnection();

            if (connectFlag == true) {
                //當連線正常時 把quartz cron調整回來
                triggerChangeStatus = changeCronTrig(connectFlag);
                log.info("Database is connected, Update trgger to default: " + triggerChangeStatus);
            }
        } catch (SQLException ex) {
            log.error(ex.toString());
            if (connectFlag == false) {
                //當連線異常時，進入這把排程調整成15分鐘看一下資料庫連線
                triggerChangeStatus = changeCronTrig(connectFlag);
                log.error("Database disconnected, Update trigger to disconnected_exp: " + triggerChangeStatus);
            }
        }
        return conn;
    }

    public static List<Map> queryForMapList(Connection conn, String sql, Object... params) {
        return query(conn, new MapListHandler(), sql, params);
    }

    public static List queryForBeanList(Connection conn, Class cls, String sql, Object... params) {
        return query(conn, new BeanListHandler(cls), sql, params);
    }

    public static List<Array> queryForArrayList(Connection conn, String sql, Object... params) {
        return query(conn, new ArrayListHandler(), sql, params);
    }

    private static List query(Connection conn, ResultSetHandler rsh, String sql, Object... params) {
        List<?> data = null;
        qRunner = new QueryRunner();
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
        qRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Object o : l) {
                    qRunner.fillStatementWithBean(ps, o, params);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            DbUtils.commitAndClose(conn);
            flag = true;
        } catch (Exception e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            log.error(e.toString());
        }
        return flag;
    }

    public static boolean update(Connection conn, String sql, Object... params) {
        boolean flag = false;
        qRunner = new QueryRunner();
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
        pRunner = new ProcRunner();
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

    public static List<Map> queryProcForMapList(Connection conn, String sql, Object... params) {
        return queryProc(conn, new MapListHandler(), sql, params);
    }

    private static List queryProc(Connection conn, ResultSetHandler rsh, String sql, Object... params) {
        List data = null;
        pRunner = new ProcRunner();
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
    private static boolean changeCronTrig(boolean connectionStatus) {
        try {
            CronTrigMod c = CronTrigMod.getInstance();
            if (connectionStatus == true) {
                c.updateMainJobCronExpressionToDefault();
            } else {
                c.updateMainJobCronExpression();
            }
            connectFlag = !connectFlag;//有更改時調整flag(開關)做判斷，避免重複修改
            return true;
        } catch (SchedulerException e) {
            log.error(e.toString());
            return false;
        }
    }

    public static void objectInit() {
        try {
            for (String key : dataSourceMap.keySet()) {
                DataSource ds = dataSourceMap.get(key);
                if (ds instanceof PooledDataSource) {
                    PooledDataSource pds = (PooledDataSource) ds;
                    pds.close();
                }
                DataSources.destroy(ds);
                Thread.sleep(1000);
            }
            dataSourceMap.clear();
        } catch (SQLException | InterruptedException ex) {
            log.error(ex.toString());
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Line;
import com.advantech.entity.LineOwnerMapping;
import com.advantech.service.BasicService;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;

/**
 *
 * @author Wei.Cheng
 */
public class LineOwnerMappingDAO extends BasicDAO {

    public LineOwnerMappingDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<LineOwnerMapping> query(String sql, Object... params) {
        return queryForBeanList(this.getConn(), LineOwnerMapping.class, sql, params);
    }

    public List<LineOwnerMapping> getAll() {
        return this.query("SELECT * FROM LineOwnerMapping");
    }

    public List<LineOwnerMapping> getOne(int id) {
        return this.query("SELECT * FROM LineOwnerMapping WHERE id = ?", id);
    }

    public List<LineOwnerMapping> getByLine(int lineId) {
        return this.query("SELECT * FROM LineOwnerMapping WHERE line_id = ?", lineId);
    }

    public List<Map> getLineOwnerMappingView() {
        return queryForMapList(this.getConn(), "SELECT * FROM lineOwnerMappingView");
    }

    public List<Map> getLineNotSetting() {
        return queryForMapList(this.getConn(), "SELECT * FROM LineOwnerMappingView WHERE line_id IS NULL");
    }

    public List<Map> getResponsorPerSitefloorView() {
        return queryForMapList(this.getConn(), "SELECT * FROM responsorPerSitefloorView");
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit1();
        Integer lineId = 1;
        Line line = BasicService.getLineService().getLine(lineId);
        JSONArray responsors = new JSONArray();

        List<Map> l = BasicService.getLineOwnerMappingService().getLineNotSetting();

        for (Map m : l) {
            if (m.containsKey("sitefloor") && m.containsKey("sensor_alarm") && m.containsKey("user_name")) {
                Integer line_id = (Integer) m.get("line_id");
                Integer sitefloor = (Integer) m.get("sitefloor");
                Integer sensor_alarm = (Integer) m.get("sensor_alarm");
                String user_name = (String) m.get("user_name");

                if ((line_id != null && line_id == line.getId()) || (line_id == null && sensor_alarm == 1 && (sitefloor == null || sitefloor == line.getSitefloor()))) {
                    responsors.put(user_name);
                }
            }
        }

        System.out.println(responsors);
    }
}

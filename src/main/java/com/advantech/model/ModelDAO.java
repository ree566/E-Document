/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Model;
import static com.advantech.model.BasicDAO.getDBUtilConn;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ModelDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);

    public ModelDAO() {

    }

    private Connection getConn() {
        return getDBUtilConn(BasicDAO.SQL.E_DOC);
    }

    public List<Map> getAll() {
        return queryForMapList(getConn(), "SELECT * FROM ModelDAO");
    }

    public List<Map> getOne(Model model) {
        return queryForMapList(getConn(), "SELECT * FROM ModelDAO WHERE id = ?", model.getId());
    }

    public boolean add(Model model) {
        List l = new ArrayList();
        l.add(model);
        return update(this.getConn(), "INSERT INTO Model(name) values(?)", l, "name");
    }

    public boolean update(Model model) {
        List l = new ArrayList();
        l.add(model);
        return update(this.getConn(), "UPDATE Model SET name = ? WHERE id = ?", l, "name", "id");
    }

    public boolean delete(Model model) {
        List l = new ArrayList();
        l.add(model);
        return update(this.getConn(), "DELETE Model WHERE id =?", l, "id");
    }
}

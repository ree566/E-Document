/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.BasicDAO;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) {
        Connection conn = null;
        DataSource ds = null;
        try {
            ds = BasicDAO.getDataSource1(BasicDAO.SQL.Way_Chien_TWM3.toString());
        } catch (NamingException ex) {
            out.println("Get DataSource fail.");
        }
        do {
            try {
                conn = ds.getConnection();
            } catch (SQLException ex) {
                out.println("Connect fail.");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException ex1) {
                    out.println(ex1);
                }
            }
        } while (conn == null);
    }
}

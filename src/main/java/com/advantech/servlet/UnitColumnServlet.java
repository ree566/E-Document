/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.service.SheetEEService;
import com.advantech.service.SheetIEService;
import com.advantech.service.SheetSPEService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "UnitColumnServlet", urlPatterns = {"/UnitColumnServlet"})
public class UnitColumnServlet extends HttpServlet {

    private final String SPE = "SPE", EE = "EE", IE = "IE";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req
     * @param res
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");

        PrintWriter out = res.getWriter();

        String unit = req.getParameter("unit");

        if (unit != null && !"".equals(unit)) {
            unit = unit.toUpperCase();
            List<String> columnName;
            switch (unit) {
                case SPE:
                    columnName = new SheetSPEService().getColumnName();
                    break;
                case EE:
                    columnName = Arrays.asList(new SheetEEService().getColumnName());
                    break;
                case IE:
                    columnName = Arrays.asList(new SheetIEService().getColumnName());
                    break;
                default:
                    columnName = new ArrayList();
                    break;
            }
            out.print(new JSONArray(columnName));
        } else {
            out.print("Param not found");
        }
    }
}

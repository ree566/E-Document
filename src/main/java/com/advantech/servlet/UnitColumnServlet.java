/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.JsonHelper;
import com.advantech.service.SheetEEService;
import com.advantech.service.SheetIEService;
import com.advantech.service.SheetSPEService;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            List l;
            switch (unit) {
                case SPE:
                    l = (List) new SheetSPEService().findAll();
                    break;
                case EE:
                    l = (List) new SheetEEService().findAll();
                    break;
                case IE:
                    l = (List) new SheetIEService().findAll();
                    break;
                default:
                    l = new ArrayList();
                    break;
            }
            List columns = this.getColumnNames(l);
            out.print(JsonHelper.toJSON(columns));
        } else {
            out.print("Param not found");
        }
    }

    private List<String> getColumnNames(List<Object> l) {
        List<String> list = new ArrayList();
        if (!l.isEmpty()) {
            Object obj = l.get(0);
            for (Field field : obj.getClass().getDeclaredFields()) {
                l.add(field.getName());
            }
        }
        return list;
    }
}

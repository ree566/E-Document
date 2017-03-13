/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.entity.Model;
import com.advantech.service.ModelService;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "ReceiveSheetMoidfied", urlPatterns = {"/ReceiveSheetMoidfied"})
public class ReceiveSheetMoidfied extends HttpServlet {

    private final String ADD = "add";
    private final String DELETE = "del";
    private final String EDIT = "edit";

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req
     * @param res
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
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

        String oper = req.getParameter("oper"); //Get the user operation(CRUD)
        HttpSession session = req.getSession();
        String unit = (String) session.getAttribute("unit");

//        if (unit != null && !"".equals(unit)) {
        if (true) {
            ModelService service = new ModelService();
            String model_id = req.getParameter("id");
            String modelName = req.getParameter("Model");
            boolean modifyFlag = false;
            switch (oper) {
                case ADD:
//                    modifyFlag = service.add(new Model(modelName));
                    break;
                case EDIT:
//                    modifyFlag = service.update(new Model(Integer.parseInt(model_id), modelName));
                    break;
                case DELETE:
//                    modifyFlag = service.delete(new Model(Integer.parseInt(model_id), modelName));
                    break;
                default:
                    break;
            }
            res.setStatus(modifyFlag == true ? HttpServletResponse.SC_ACCEPTED : HttpServletResponse.SC_NOT_MODIFIED);
            res.getWriter().print(new JSONObject().put("STATUS", "OK"));
        } else {
            Enumeration params = req.getParameterNames();
            while (params.hasMoreElements()) {
                String paramName = (String) params.nextElement();
                System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
            }
            res.getWriter().print(new JSONObject().put("status", "OK"));
        }
    }
}

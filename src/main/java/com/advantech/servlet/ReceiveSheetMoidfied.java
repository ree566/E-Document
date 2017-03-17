/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.model.Model;
import com.advantech.service.ModelService;
import com.advantech.service.SheetEEService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
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
    private final String EE = "EE";
    private final String IE = "IE";
    private final String SPE = "SPE";
    private String unit;

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
        unit = (String) session.getAttribute("unit");

        if (unit != null && !"".equals(unit)) {

            this.showParams(req, res);

            switch (oper) {
                case ADD:
                    this.addRow(req, res);
                    break;

                case EDIT:
                    this.editRow(req, res);
                    break;

                case DELETE:
                    this.deleteRow(req, res);
                    break;

                default:
                    break;
            }
//            res.setStatus(modifyFlag == true ? HttpServletResponse.SC_ACCEPTED : HttpServletResponse.SC_NOT_MODIFIED);
        } else {
            res.getWriter().print(new JSONObject().put("status", "NO UNIT FOUND"));
        }
    }

    private void addRow(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String modelName = req.getParameter("Model");
        ModelService modelService = new ModelService();
        if (modelService.findByName(modelName).isEmpty()) {
            modelService.insert(new Model(0, modelName));
            switch (unit) {
                case EE:
                    SheetEEService eeService = new SheetEEService();
                    break;
                case IE:
                    break;
                case SPE:
                    break;
                default:
                    break;
            }
            out.print("SUCCESS");
        } else {
            out.print("This model is already exist.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void editRow(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String Model_id = req.getParameter("id");
        String modelName = req.getParameter("Model");
        String type = req.getParameter("Type");
        String cleanPanel = req.getParameter("CleanPanel");
        String assy = req.getParameter("ASSY");
        String packing = req.getParameter("Packing");
        String bICost = req.getParameter("BI_Cost");
        String vibration = req.getParameter("Vibration");
        String hiPotLeakage = req.getParameter("Hi_Pot_Leakage");
        String coldBoot = req.getParameter("Cold_Boot");
        String warmBoot = req.getParameter("Warm_Boot");
        String aSSYToT1 = req.getParameter("ASSY_to_T1");
        String t2ToPacking = req.getParameter("T2_to_Packing");
        String floor = req.getParameter("Floor");
        String pending = req.getParameter("Pending");
        String pendingTime = req.getParameter("Pending_Time");
        String burnIn = req.getParameter("BurnIn");
        String bITime = req.getParameter("BI_Time");
        String bITemperature = req.getParameter("BI_Temperature");
        String speOwner = req.getParameter("SPE_Owner");
        String eeOwner = req.getParameter("EE_Owner");
        String qcOwner = req.getParameter("QC_Owner");
        String ASSY_Packing_SOP = req.getParameter("ASSY_Packing_SOP");
        String keypartA = req.getParameter("Keypart_A");
        String keypartB = req.getParameter("Keypart_B");
        String preAssy = req.getParameter("Pre_ASSY");
        String babFlow = req.getParameter("BAB_Flow");
        String testFlow = req.getParameter("Test_Flow");
        String packingFlow = req.getParameter("Packing_Flow");
        String partLink = req.getParameter("Part_Link");
        String ce = req.getParameter("CE");
        String ul = req.getParameter("UL");
        String rohs = req.getParameter("ROHS");
        String weee = req.getParameter("WEEE");
        String mit = req.getParameter("Made_in_Taiwan");
        String fcc = req.getParameter("FCC");
        String fac = req.getParameter("EAC");
        String n1collectionBox = req.getParameter("N_in_1_collection_box");
        String pNoMaintain = req.getParameter("PartNo_attr_maintain");
    }

    private void deleteRow(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String Model_id = req.getParameter("id");
        ModelService modelService = new ModelService();
        if (Model_id.contains(",")) {
            boolean flag = true;

            List<Model> l = modelService.findByPrimaryKeys(separateIds(Model_id));
            for (Model m : l) {
                boolean deleteSuccess = modelService.delete(m) == 1;
                if (!deleteSuccess) {
                    flag = false;
                }
            }
            res.setStatus(flag ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Model m = (Model) modelService.findByPrimaryKey(Integer.parseInt(Model_id));
            if (modelService.delete(m) == 1) {
                out.print("SUCCESS");
            } else {
                out.print("FAIL");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private Integer[] separateIds(String str) {
        String[] strArray = str.split(",");
        Integer[] intArray = new Integer[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        return intArray;
    }

    private void showParams(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }
    }
}

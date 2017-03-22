/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import com.advantech.model.Model;
import com.advantech.model.SheetEe;
import com.advantech.model.SheetIe;
import com.advantech.model.SheetSpe;
import com.advantech.response.SheetViewResponse;
import com.advantech.service.ModelService;
import com.advantech.service.SheetEEService;
import com.advantech.service.SheetIEService;
import com.advantech.service.SheetSPEService;
import com.advantech.service.SheetViewService;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@SessionAttributes({"user"})
public class SheetViewController {

    private final String SPE = "SPE", EE = "EE", IE = "IE";

    private final String ADD = "add", EDIT = "edit", DELETE = "del";
    private String userOper;

    @Autowired
    private SheetViewService sheetViewService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private SheetEEService eeService;

    @Autowired
    private SheetIEService ieService;

    @Autowired
    private SheetSPEService speService;

    @ResponseBody
    @RequestMapping(value = "/getSheetView.do", method = {RequestMethod.POST})
    public SheetViewResponse getSheetView(@ModelAttribute PageInfo info) {
        List l = (List) sheetViewService.findAll(info);
        SheetViewResponse viewResp = new SheetViewResponse();

        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;

        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());

        return viewResp;
    }

    @ResponseBody
    @RequestMapping(value = "/updateSheet.do", method = {RequestMethod.POST})
    public String updateSheet(
            @RequestParam String oper,
            @RequestParam int id, @RequestParam String modelName,
            @ModelAttribute SheetSpe spe,
            @ModelAttribute SheetEe ee,
            @ModelAttribute SheetIe ie,
            @ModelAttribute("user") Identit user,
            HttpServletRequest req) throws ServletException, IOException {

        this.showParams(req);
        this.printModels(id, spe, ee, ie);

        String userType = user.getUserType().getName();

        Model model = id == 0 ? new Model(modelName) : (Model) modelService.findByPrimaryKey(id);
        userOper = oper;

        String modifyMessage;
        switch (userType) {
            case EE:
                modifyMessage = this.eeModify(model, ee);
                break;
            case IE:
                modifyMessage = this.ieModify(model, ie);
                break;
            case SPE:
                modifyMessage = this.speModify(model, spe);
                break;
            default:
                modifyMessage = "Unit not found";
                break;
        }
        return modifyMessage;
    }

    private String ieModify(Model m, Object sheet) {
        switch (userOper) {
            case ADD:
                break;
            case EDIT:
                break;
            case DELETE:
                break;
            default:
                break;
        }
        return "";
    }

    private String eeModify(Model m, Object sheet) {
        switch (userOper) {
            case ADD:
                break;
            case EDIT:
                break;
            case DELETE:
                break;
            default:
                break;
        }
        return "";
    }

    private String speModify(Model m, Object sheet) {
        Set set = new HashSet();
        switch (userOper) {
            case ADD:
                set.add(sheet);
                m.setSheetSpes(set);
                modelService.insert(m);
                break;
            case EDIT:
                Set speSet = m.getSheetSpes();
                Iterator iter = speSet.iterator();
                SheetSpe existSheetSpe = (SheetSpe) iter.next();
                SheetSpe newData = (SheetSpe) sheet;
                newData.setId(existSheetSpe.getId());
                speService.update(newData);
                break;
            case DELETE:
                modelService.delete(m);
            default:
                throw new UnsupportedOperationException();
        }
        return "";

    }

    @ResponseBody
    @RequestMapping(value = "/unitColumnServlet.do", method = {RequestMethod.POST})
    public String[] getUnitColumnName(@RequestParam String unit) {
        unit = unit.toUpperCase();
        String[] columnName;
        switch (unit) {
            case SPE:
                columnName = speService.getColumnName();
                break;
            case EE:
                columnName = eeService.getColumnName();
                break;
            case IE:
                columnName = ieService.getColumnName();
                break;
            default:
                columnName = new String[0];
                break;
        }
        return columnName;
    }

    private void printModels(Object... model) {
        System.out.println(new Gson().toJson(model));
    }

    private void showParams(HttpServletRequest req) throws ServletException, IOException {
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }
    }
}

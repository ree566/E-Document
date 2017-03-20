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
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @ResponseBody
    @RequestMapping(value = "/getSheetView.do", method = {RequestMethod.GET, RequestMethod.POST})
    public SheetViewResponse getSheetView(@ModelAttribute PageInfo info) {
        SheetViewService service = new SheetViewService();
        List l = (List) service.findAll(info);

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
    @RequestMapping(value = "/updateSheet.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateSheet(@ModelAttribute Model model,
            @ModelAttribute SheetSpe spe,
            @ModelAttribute SheetEe ee,
            @ModelAttribute SheetIe ie,
            @ModelAttribute("user") Identit user,
            HttpServletRequest req) throws ServletException, IOException {

        this.showParams(req);
        this.printModels(model, spe, ee, ie);

//        String userType = user.getUserType().getName();
//        ModelService modelService = new ModelService();
//        Model existModel = (Model) modelService.findByPrimaryKey(model.getId());
//        switch (userType) {
//            case EE:
//                if (existModel == null) {
//                    Set set = new HashSet();
//                    model.setSheetEes(set);
//                    modelService.insert(model);
//                } else {
//                    Set set = existModel.getSheetEes() == null ? new HashSet() : existModel.getSheetEes();
//                    set.add(ee);
//                    modelService.update(existModel);
//                }
//                break;
//            case IE:
//                if (existModel == null) {
//                    Set set = new HashSet();
//                    model.setSheetIes(set);
//                    modelService.insert(model);
//                } else {
//                    Set set = existModel.getSheetIes() == null ? new HashSet() : existModel.getSheetIes();
//                    set.add(ie);
//                    modelService.update(existModel);
//                }
//                break;
//            case SPE:
//                if (existModel == null) {
//                    Set set = new HashSet();
//                    model.setSheetSpes(set);
//                    modelService.insert(model);
//                } else {
//                    Set set = existModel.getSheetSpes() == null ? new HashSet() : existModel.getSheetSpes();
//                    set.add(spe);
//                    modelService.update(existModel);
//                }
//                break;
//            default:
//                break;
//        }
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/unitColumnServlet.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<String> updateSheet(@RequestParam String unit) {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Floor;
import com.advantech.model.Identit;
import com.advantech.model.Model;
import com.advantech.model.SheetIe;
import com.advantech.model.SheetSpe;
import com.advantech.model.Type;
import com.advantech.service.FloorService;
import com.advantech.service.IdentitService;
import com.advantech.service.ModelService;
import com.advantech.service.SheetIEService;
import com.advantech.service.SheetSPEService;
import com.advantech.service.TypeService;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class SheetModifyController {

    private final String SPE = "SPE", EE = "EE", IE = "IE";
    private final String ADD = "add", EDIT = "edit", DELETE = "del";
    private String userOper;
    private final String SUCCESS_MESSAGE = "SUCCESS";
    private final String FAIL_MESSAGE = "FAIL";

    @Autowired
    private ModelService modelService;

    @Autowired
    private SheetIEService ieService;

    @Autowired
    private SheetSPEService speService;

    @Autowired
    private IdentitService identitService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private TypeService typeService;

    @RequestMapping(value = "/updateSheet.do", method = {RequestMethod.POST})
    public ResponseEntity updateSheet(
            @RequestParam String oper,
            @RequestParam String modelId, @RequestParam(required = false) String modelName,
            @ModelAttribute SheetIe ie,
            @ModelAttribute SheetSpe spe,
            @ModelAttribute("user") Identit user,
            @RequestParam(required = false, defaultValue = "0") int floorName, // Get the selected drop down list option from client
            @RequestParam(required = false, defaultValue = "0") int typeName, // Get the selected drop down list option from client
            @RequestParam(required = false, defaultValue = "0") int speOwnerName,
            @RequestParam(required = false, defaultValue = "0") int eeOwnerName,
            @RequestParam(required = false, defaultValue = "0") int qcOwnerName,
            HttpServletRequest req) throws ServletException, IOException {

        this.printModels(modelId, spe, ie);
        this.showParams(req);

        String modifyMessage;

        userOper = oper;

        if (this.DELETE.equals(oper)) {
            modifyMessage = this.deleteModel(modelId);
        } else {
            String userType = user.getUserType().getName();
            switch (userType) {
                case IE:
                    modifyMessage = this.ieModify(new Model(Integer.parseInt(modelId), modelName), ie);
                    break;
                case SPE:
                    spe.setFloor((Floor) floorService.findByPrimaryKey(floorName));
                    spe.setType(typeName == 0 ? null : (Type) typeService.findByPrimaryKey(typeName));
                    spe.setIdentitBySpeOwnerId(speOwnerName == 0 ? null : (Identit) identitService.findByPrimaryKey(speOwnerName));
                    spe.setIdentitByEeOwnerId(eeOwnerName == 0 ? null : (Identit) identitService.findByPrimaryKey(eeOwnerName));
                    spe.setIdentitByQcOwnerId(qcOwnerName == 0 ? null : (Identit) identitService.findByPrimaryKey(qcOwnerName));
                    modifyMessage = this.speModify(new Model(Integer.parseInt(modelId), modelName), spe);
                    break;
                default:
                    modifyMessage = "Unsupport unit";
                    break;
            }
        }
        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

    private String checkAndInsertModel(Model model) {
        Model existModel = modelService.findByName(model.getName());
        if (existModel == null) {
            modelService.insert(model);
            model = modelService.findByName(model.getName());
            return model != null ? this.SUCCESS_MESSAGE : "insert fail";
        } else {
            return "Model is already exist.";
        }
    }

    private String deleteModel(String modelId) {
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        List<String> ids = splitter.splitToList(modelId);
        Integer[] id = new Integer[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            id[i] = Integer.valueOf(ids.get(i));
        }
        List<Model> l = modelService.findByPrimaryKeys(id);
        modelService.delete(l);
        return this.SUCCESS_MESSAGE;
    }

    private String ieModify(Model model, SheetIe sheet) {
        switch (userOper) {
            case ADD:
                String checkMessage = checkAndInsertModel(model);
                if (checkMessage.equals(this.SUCCESS_MESSAGE)) {
                    sheet.setModel(model);
                    return ieService.insert(sheet) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
                } else {
                    return checkMessage;
                }
            case EDIT:
                return ieService.update(model, sheet) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
            default:
                return "Unsupport action";
        }
    }

    private String speModify(Model model, SheetSpe sheet) {
        switch (userOper) {
            case ADD:
                String checkMessage = checkAndInsertModel(model);
                if (checkMessage.equals(this.SUCCESS_MESSAGE)) {
                    sheet.setModel(model);
                    return speService.insert(sheet) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
                } else {
                    return checkMessage;
                }
            case EDIT:
                return speService.update(model, sheet) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
            default:
                return "Unsupport action";
        }
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

    @ResponseBody
    @RequestMapping(value = "/test.do", method = {RequestMethod.GET})
    public String test() {

        speService.findByPrimaryKey(2);

        return "";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Identit;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeColumnGroup;
import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.IdentitService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.advantech.service.WorktimeColumnGroupService;
import com.advantech.service.WorktimeService;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private WorktimeService worktimeService;

    @Autowired
    private IdentitService identitService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private WorktimeColumnGroupService worktimeColumnGroupService;

    @RequestMapping(value = "/updateSheet.do", method = {RequestMethod.POST})
    public ResponseEntity updateSheet(
            @RequestParam String oper,
            @RequestParam String rowId,
            @ModelAttribute Worktime worktime,
            @ModelAttribute("user") Identit user,
            @RequestParam int typeName, // Get the selected drop down list option from client
            @RequestParam int floorName, // Get the selected drop down list option from client
            @RequestParam int speOwnerName, // Get the selected drop down list option from client
            @RequestParam int eeOwnerName, // Get the selected drop down list option from client
            @RequestParam int qcOwnerName, // Get the selected drop down list option from client
            @RequestParam int pendingName, // Get the selected drop down list option from client
            @RequestParam(required = false, defaultValue = "0") int preAssyName, // Get the selected drop down list option from client
            @RequestParam(required = false, defaultValue = "0") int babFlow, // Get the selected drop down list option from client
            @RequestParam(required = false, defaultValue = "0") int testFlow, // Get the selected drop down list option from client
            @RequestParam(required = false, defaultValue = "0") int packingFlow, // Get the selected drop down list option from client
            HttpServletRequest req,
            BindingResult errors) throws ServletException, IOException {
        
        if (errors.hasErrors()) {
            // error handling code goes here.
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errors.getFieldErrors());
        }

        this.printModels(worktime);
        this.showParams(req);

        String modifyMessage;

        userOper = oper;

        if (oper.equals(DELETE)) {
            modifyMessage = this.deleteRows(rowId);
        } else {
            String userType = user.getUserType().getName();
            switch (userType) {
                case IE:
                case SPE:
                    worktime.setId(Integer.parseInt(rowId));
                    if (isModelExists(worktime)) {
                        modifyMessage = "This model name is already exists";
                    } else {
                        worktime.setFloor(floorService.findByPrimaryKey(floorName));
                        worktime.setType(typeService.findByPrimaryKey(typeName));
                        worktime.setIdentitBySpeOwnerId(identitService.findByPrimaryKey(speOwnerName));
                        worktime.setIdentitByEeOwnerId(identitService.findByPrimaryKey(eeOwnerName));
                        worktime.setIdentitByQcOwnerId(identitService.findByPrimaryKey(qcOwnerName));
                        worktime.setPending(pendingService.findByPrimaryKey(pendingName));
                        worktime.setPreAssy(preAssyName == 0 ? null : preAssyService.findByPrimaryKey(preAssyName));
                        worktime.setFlowByBabFlowId(babFlow == 0 ? null : flowService.findByPrimaryKey(babFlow));
                        worktime.setFlowByTestFlowId(testFlow == 0 ? null : flowService.findByPrimaryKey(testFlow));
                        worktime.setFlowByPackingFlowId(packingFlow == 0 ? null : flowService.findByPrimaryKey(packingFlow));
                        worktime.setModifiedDate(new Date());
                        modifyMessage = this.updateRows(worktime);
                    }
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

    private String deleteRows(String rowId) {
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        List<String> ids = splitter.splitToList(rowId);
        Integer[] id = new Integer[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            id[i] = Integer.valueOf(ids.get(i));
        }
        List<Worktime> l = worktimeService.findByPrimaryKeys(id);
        worktimeService.delete(l);
        return this.SUCCESS_MESSAGE;
    }

    private String updateRows(Worktime worktime) {
        switch (userOper) {
            case ADD:
                return worktimeService.insert(worktime) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
            case EDIT:
                return worktimeService.update(worktime) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
            default:
                return "Unsupport action";
        }
    }

    private boolean isModelExists(Worktime worktime) {
        Worktime existWorktime = worktimeService.findByModel(worktime.getModelName());
        if (worktime.getId() == 0) {
            return existWorktime != null;
        } else {
            return existWorktime != null && existWorktime.getId() != worktime.getId();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unitColumnServlet.do", method = {RequestMethod.POST})
    public String[] getUnitColumnName(@ModelAttribute("user") Identit user) {
        int unit = user.getUserType().getId();

        String[] columnName;

        WorktimeColumnGroup w = worktimeColumnGroupService.findByUserType(unit);

        try {
            Clob columns = w.getColumnName();
            String clobString = columns.getSubString(1, (int) columns.length());
            columnName = clobString.split(",");
            return columnName;
        } catch (SQLException ex) {
            return new String[0];
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeColumnGroup;
import com.advantech.response.JqGridResponse;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeColumnGroupService;
import com.advantech.service.WorktimeService;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/Worktime")
public class WorktimeController extends CrudController<Worktime> {

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SheetViewService sheetViewService;

    @Autowired
    private WorktimeColumnGroupService worktimeColumnGroupService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_GUEST"})
    @Override
    public JqGridResponse findAll(PageInfo info) {
        return toJqGridResponse(worktimeService.findAll(info), info);
    }

    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Override
    public ResponseEntity update(
            @RequestParam
            final String oper,
            @Valid @ModelAttribute Worktime worktime,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(error.getField() + " " + error.getDefaultMessage());
            }
        }

        String modifyMessage;

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Unit userType = user.getUnit();
        if (userType != null) {
            switch (oper) {
                case ADD:
                    if (isModelExists(worktime)) {
                        modifyMessage = "This model name is already exists";
                    } else {
                        resetNullableColumn(worktime);
                        modifyMessage = worktimeService.insert(worktime) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
                    }
                    break;
                case EDIT:
                    if (isModelExists(worktime)) {
                        modifyMessage = "This model name is already exists";
                    } else {
                        resetNullableColumn(worktime);
                        modifyMessage = worktimeService.merge(worktime) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
                    }
                    break;
                case DELETE:
                    modifyMessage = worktimeService.delete(worktime) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                    break;
                default:
                    modifyMessage = "Unsupport action";
                    break;
            }
        } else {
            modifyMessage = "Unsupport unit";
        }

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

    private void resetNullableColumn(Worktime worktime) {
        if (worktime.getPreAssy().getId() == 0) {
            worktime.setPreAssy(null);
        }

        if (worktime.getFlowByBabFlowId().getId() == 0) {
            worktime.setFlowByBabFlowId(null);
        }

        if (worktime.getFlowByTestFlowId().getId() == 0) {
            worktime.setFlowByTestFlowId(null);
        }

        if (worktime.getFlowByPackingFlowId().getId() == 0) {
            worktime.setFlowByPackingFlowId(null);
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
    @RequestMapping(value = "/unitColumn", method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String[] getUnitColumnName() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int unit = user.getUnit().getId();

        String[] columnName;

        WorktimeColumnGroup w = worktimeColumnGroupService.findByUserType(unit);

        try {
            Clob columns = w.getColumnName();
            if (columns == null) {
                return new String[0];
            } else {
                String clobString = columns.getSubString(1, (int) columns.length());
                columnName = clobString.split(",");
                return columnName;
            }
        } catch (SQLException ex) {
            return new String[0];
        }
    }

    @ResponseBody
    @RequestMapping(value = "/excel", method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ModelAndView generateExcel() {
        // create some sample data
        List<SheetView> l = sheetViewService.findAll();
        return new ModelAndView("ExcelRevenueSummary", "revenueData", l);
    }

    //    private String deleteRows(String rowId) {
//        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
//        List<String> ids = splitter.splitToList(rowId);
//        Integer[] id = new Integer[ids.size()];
//        for (int i = 0; i < ids.size(); i++) {
//            id[i] = Integer.valueOf(ids.get(i));
//        }
//        List<Worktime> l = worktimeService.findByPrimaryKeys(id);
//        worktimeService.delete(l);
//        return this.SUCCESS_MESSAGE;
//    }
}

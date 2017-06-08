/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import com.advantech.response.JqGridResponse;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_GUEST"})
    @Override
    public JqGridResponse read(PageInfo info) {
        return toJqGridResponse(worktimeService.findAll(info), info);
    }
    
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Override
    protected ResponseEntity insert(Worktime worktime, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return serverResponse(bindingResult.getFieldErrors());
        }
        
        String modifyMessage;
        
        if (isModelExists(worktime)) {
            modifyMessage = "This model name is already exists";
        } else {
            resetNullableColumn(worktime);
            modifyMessage = worktimeService.insertWithFormulaSetting(worktime) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
        }
        
        return serverResponse(modifyMessage);
    }
    
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Override
    public ResponseEntity update(@Valid @ModelAttribute Worktime worktime, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return serverResponse(bindingResult.getFieldErrors());
        }
        
        String modifyMessage;
        if (isModelExists(worktime)) {
            modifyMessage = "This model name is already exists";
        } else {
            resetNullableColumn(worktime);
            modifyMessage = worktimeService.merge(worktime) == 1 ? this.SUCCESS_MESSAGE : FAIL_MESSAGE;
        }
        
        return serverResponse(modifyMessage);
    }
    
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Override
    protected ResponseEntity delete(int id) {
        String modifyMessage = worktimeService.delete(id) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
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
        
        if (worktime.getUserByEeOwnerId().getId() == 0) {
            worktime.setUserByEeOwnerId(null);
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
    @RequestMapping(value = "/excel", method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ModelAndView generateExcel(PageInfo info) {
        // create some sample data
        List<SheetView> l = sheetViewService.findAll(info);
        return new ModelAndView("ExcelRevenueSummary", "revenueData", l);
    }
    
}

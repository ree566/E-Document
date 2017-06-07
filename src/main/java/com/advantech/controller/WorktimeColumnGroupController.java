/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.model.WorktimeColumnGroup;
import com.advantech.response.JqGridResponse;
import com.advantech.service.WorktimeColumnGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/WorktimeColumnGroup")
public class WorktimeColumnGroupController extends CrudController<WorktimeColumnGroup> {

    @Autowired
    private WorktimeColumnGroupService worktimeColumnGroupService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse read(PageInfo info) {
        return toJqGridResponse(worktimeColumnGroupService.findAll(info), info);
    }

    @ResponseBody
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity insert(WorktimeColumnGroup pojo, BindingResult bindingResult) {
        String modifyMessage;
        if (isWorktimeColumnGroupExistsInUnit(pojo)) {
            modifyMessage = "Setting is already exists.";
        } else {
            modifyMessage = worktimeColumnGroupService.insert(pojo) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        }
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(WorktimeColumnGroup pojo, BindingResult bindingResult) {
        String modifyMessage;
        if (isWorktimeColumnGroupExistsInUnit(pojo)) {
            modifyMessage = "Setting is already exists.";
        } else {
            modifyMessage = worktimeColumnGroupService.update(pojo) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        }
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity delete(int id) {
        String modifyMessage = worktimeColumnGroupService.delete(id) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = "/byUnit", method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public WorktimeColumnGroup getUnitColumnName() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int unit = user.getUnit().getId();
        WorktimeColumnGroup w = worktimeColumnGroupService.findByUnit(unit);
        return w == null ? new WorktimeColumnGroup() : w;
    }

    private boolean isWorktimeColumnGroupExistsInUnit(WorktimeColumnGroup w) {
        Unit u = w.getUnit();
        WorktimeColumnGroup existRule = worktimeColumnGroupService.findByUnit(u.getId());
        return existRule != null && existRule.getId() != w.getId();
    }

}

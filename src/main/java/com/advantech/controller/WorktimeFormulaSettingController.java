/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.WorktimeFormulaSetting;
import com.advantech.service.WorktimeFormulaSettingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@RequestMapping(value = "/WorktimeFormulaSetting")
public class WorktimeFormulaSettingController {

    @Autowired
    private WorktimeFormulaSettingService worktimeFormulaSettingService;

    @ResponseBody
    @RequestMapping(value = "/find/{worktimeId}", method = {RequestMethod.GET})
    protected WorktimeFormulaSetting read(@PathVariable(value = "worktimeId") int worktimeId) {
        List<WorktimeFormulaSetting> l = worktimeFormulaSettingService.findByWorktime(worktimeId);
        return l.isEmpty() ? null : l.get(0);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import static com.advantech.helper.SecurityPropertiesUtils.retrieveAndCheckUserInSession;
import com.advantech.model.Line;
import com.advantech.model.ModelSopRemark;
import com.advantech.model.ModelSopRemarkDetail;
import com.advantech.model.ModelSopRemarkEvent;
import com.advantech.model.User;
import com.advantech.service.ModelSopRemarkDetailService;
import com.advantech.service.ModelSopRemarkEventService;
import com.advantech.service.ModelSopRemarkService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/ModelSopRemarkController")
public class ModelSopRemarkController {

    private static final Logger log = LoggerFactory.getLogger(ModelSopRemarkController.class);

    @Autowired
    private ModelSopRemarkService modelSopRemarkService;

    @Autowired
    private ModelSopRemarkDetailService modelSopRemarkDetailService;

    @Autowired
    private ModelSopRemarkEventService modelSopRemarkEventService;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findAll(HttpServletRequest request) {
        List l;
        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_OPER_IE")) {
            l = modelSopRemarkService.findAll();
        } else {
            User user = retrieveAndCheckUserInSession();
            l = user == null ? modelSopRemarkService.findAll() : modelSopRemarkService.findByUser(user);
        }

        return new DataTableResponse(l);

    }

    @RequestMapping(value = "/findUseLine", method = {RequestMethod.GET})
    @ResponseBody
    protected List<Line> findUseLine(@Valid @ModelAttribute ModelSopRemark pojo) {
        return modelSopRemarkService.findUseLine(pojo.getId());
    }

    @RequestMapping(value = "/findByTagName", method = {RequestMethod.GET})
    @ResponseBody
    protected List<ModelSopRemarkDetail> findByTagName(
            @RequestParam String tagName
    ) {
        return modelSopRemarkDetailService.findByTagName(tagName);
    }

    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveOrUpdate(@Valid @ModelAttribute ModelSopRemark pojo) {
        if (pojo.getId() == 0) {
            modelSopRemarkService.insert(pojo);
            addEvent(pojo, "insert");
        } else {
            modelSopRemarkService.update(pojo);
            addEvent(pojo, "update");
        }
        return "success";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@RequestParam int id) {
        ModelSopRemark pojo = modelSopRemarkService.findByPrimaryKey(id);
        modelSopRemarkService.delete(pojo);
        return "success";
    }

    @RequestMapping(value = "/findDetail", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findDetail(@RequestParam int id) {
        List l = modelSopRemarkService.findDetail(id);
        return new DataTableResponse(l);
    }

    @RequestMapping(value = "/saveOrUpdateDetail", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveOrUpdateDetail(@Valid @ModelAttribute ModelSopRemarkDetail detail) {
        if (detail.getId() == 0) {
            modelSopRemarkDetailService.insert(detail);
            addEvent(detail.getModelSopRemark(), "insert");
        } else {
            modelSopRemarkDetailService.update(detail);
            addEvent(detail.getModelSopRemark(), "update");
        }
        return "success";
    }

    @RequestMapping(value = "/deleteDetail", method = {RequestMethod.POST})
    @ResponseBody
    protected String deleteDetail(@RequestParam int id) {
        ModelSopRemarkDetail pojo = modelSopRemarkDetailService.findByPrimaryKey(id);
        modelSopRemarkDetailService.delete(pojo);
        addEvent(pojo.getModelSopRemark(), "delete");
        return "success";
    }

    private void addEvent(ModelSopRemark pojo, String action) {
        try {
            User user = retrieveAndCheckUserInSession();
            modelSopRemarkEventService.insert(new ModelSopRemarkEvent(pojo, user, action));
        } catch (RuntimeException e) {
            log.error("Error cause when write event", e);
        }
    }

}

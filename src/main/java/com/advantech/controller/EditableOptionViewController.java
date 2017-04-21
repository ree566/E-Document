/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import com.advantech.model.Identit;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.response.JqGridResponse;
import com.advantech.service.FlowGroupService;
import com.advantech.service.FlowService;
import com.advantech.service.IdentitService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class EditableOptionViewController {

    private static final Logger log = LoggerFactory.getLogger(EditableOptionViewController.class);

    private final String ADD = "add", EDIT = "edit", DELETE = "del";
    private final String SUCCESS_MESSAGE = "SUCCESS";
    private final String FAIL_MESSAGE = "FAIL";

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowGroupService flowGroupService;

    @Autowired
    private IdentitService identitService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private TypeService typeService;

    @ResponseBody
    @RequestMapping(value = "/getBla.do/{tableName}", method = {RequestMethod.GET})
    public JqGridResponse findAll(@PathVariable(value = "tableName") final String tableName, @ModelAttribute PageInfo info, @ModelAttribute("user") Identit user) throws JsonProcessingException, IOException {

        JqGridResponse viewResp;
        List l;

        switch (tableName) {
            case "Flow":
                l = flowService.findAll(info);
                break;
            case "FlowGroup":
                l = flowGroupService.findAll(info);
                break;
            case "Identit":
                l = identitService.findAll(info);
                break;
            case "Pending":
                l = pendingService.findAll(info);
                break;
            case "PreAssy":
                l = preAssyService.findAll(info);
                break;
            case "Type":
                l = typeService.findAll(info);
                break;
            default:
                l = new ArrayList();
        }

        viewResp = getResponseObject(l, info);
        return viewResp;
    }

    private JqGridResponse getResponseObject(List l, PageInfo info) {
        JqGridResponse viewResp = new JqGridResponse();
        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;
        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());
        return viewResp;
    }

    @ResponseBody
    @RequestMapping(value = "/updateBla.do/{tableName}", method = {RequestMethod.POST})
    public ResponseEntity update(
            @PathVariable(value = "tableName") final String tableName,
            @RequestParam final String oper,
            @ModelAttribute PageInfo info,
            @ModelAttribute("user") Identit user,
            @ModelAttribute Flow flow,
            @ModelAttribute Identit identit,
            @ModelAttribute Pending pending,
            @ModelAttribute PreAssy preAssy,
            @ModelAttribute Type type,
            HttpServletRequest req) throws ServletException, IOException {

        this.showParams(req);

        String modifyMessage;
        int responseFlag = 0;

        switch (tableName) {
            case "Flow":
                FlowGroup flowGroup = flow.getFlowGroup();
                flow.setFlowGroup(flowGroup == null ? null : flowGroupService.findByPrimaryKey(flowGroup.getId()));
                switch (oper) {
                    case ADD:
                        responseFlag = flowService.insert(flow);
                        break;
                    case EDIT:
                        responseFlag = flowService.update(flow);
                        break;
                    case DELETE:
                        responseFlag = flowService.delete(flowService.findByPrimaryKey(flow.getId()));
                        break;
                }
                modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                break;
            case "Identit":
                modifyMessage = this.FAIL_MESSAGE;
                break;
            case "Pending":
                switch (oper) {
                    case ADD:
                        responseFlag = pendingService.insert(pending);
                        break;
                    case EDIT:
                        responseFlag = pendingService.update(pending);
                        break;
                    case DELETE:
                        responseFlag = pendingService.delete(pendingService.findByPrimaryKey(pending.getId()));
                        break;
                }
                modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                break;
            case "PreAssy":
                modifyMessage = this.FAIL_MESSAGE;
                break;
            case "Type":
                modifyMessage = this.FAIL_MESSAGE;
                break;
            default:
                modifyMessage = this.FAIL_MESSAGE;
        }

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

    @ExceptionHandler(HttpSessionRequiredException.class)
//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The session has expired")
    public ResponseEntity handleSessionExpired() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("The session has expired.");
    }

    private void showParams(HttpServletRequest req) throws ServletException, IOException {
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }
    }
}

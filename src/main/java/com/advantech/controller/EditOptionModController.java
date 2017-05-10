/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.CustomPasswordEncoder;
import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import com.advantech.model.User;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.response.JqGridResponse;
import com.advantech.service.FlowGroupService;
import com.advantech.service.FlowService;
import com.advantech.service.UserService;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured("ROLE_ADMIN")
public class EditOptionModController {

    private static final Logger log = LoggerFactory.getLogger(EditOptionModController.class);

    private final String ADD = "add", EDIT = "edit", DELETE = "del";
    private final String SUCCESS_MESSAGE = "SUCCESS";
    private final String FAIL_MESSAGE = "FAIL";

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowGroupService flowGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private TypeService typeService;

    @ResponseBody
    @RequestMapping(value = "/getSelectOption.do/{tableName}", method = {RequestMethod.GET})
    public JqGridResponse findAll(@PathVariable(value = "tableName") final String tableName, @ModelAttribute PageInfo info) throws JsonProcessingException, IOException {

        JqGridResponse viewResp;
        List l;

        switch (tableName) {
            case "Flow":
                l = flowService.findAll(info);
                break;
            case "FlowGroup":
                l = flowGroupService.findAll(info);
                break;
            case "User":
                l = userService.findAll(info);
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

        viewResp = toJqGridResponse(l, info);
        return viewResp;
    }

    @ResponseBody
    @RequestMapping(value = "/updateSelectOption.do/{tableName}", method = {RequestMethod.POST})
    public ResponseEntity update(
            @PathVariable(value = "tableName") final String tableName,
            @RequestParam final String oper,
            @ModelAttribute PageInfo info,
            @ModelAttribute Flow flow,
            @RequestParam(required = false, defaultValue = "0") int parentFlowId,
            @ModelAttribute User user,
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
                List<Flow> l;
                switch (oper) {
                    case ADD:
                        l = (List<Flow>) flowService.findAll();
                        if (l.contains(flow)) {
                            modifyMessage = "This flow is already exist";
                        } else {
                            responseFlag = flowService.insert(flow);
                            modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                        }
                        break;
                    case EDIT:
                        l = (List<Flow>) flowService.findAll();
                        if (l.contains(flow)) {
                            modifyMessage = "This flow is already exist";
                        } else {
                            responseFlag = flowService.update(flow);
                            modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                        }
                        break;
                    case DELETE:
                        responseFlag = flowService.delete(flowService.findByPrimaryKey(flow.getId()));
                        modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                        break;
                    default:
                        modifyMessage = "unsupport action";
                }
                break;
            case "Flow_sub":
                switch (oper) {
                    case ADD:
                        List<Integer> addSubIds = new ArrayList();
                        addSubIds.add(flow.getId());
                        responseFlag = flowService.addSub(parentFlowId, addSubIds);
                        modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                        break;
                    case EDIT:
                        modifyMessage = this.FAIL_MESSAGE;
                        break;
                    case DELETE:
                        List deleteSubIds = new ArrayList();
                        deleteSubIds.add(flow.getId());
                        responseFlag = flowService.deleteSub(parentFlowId, deleteSubIds);
                        modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                        break;
                    default:
                        modifyMessage = this.FAIL_MESSAGE;
                }
                break;
            case "User":
                switch (oper) {
                    case ADD:
                        encryptPassword(user);
                        responseFlag = userService.insert(user);
                        break;
                    case EDIT:
                        User i = userService.findByJobnumber(user.getJobnumber());
                        if (!user.getPassword().equals(i.getPassword())) {
                            encryptPassword(user);
                        }
                        user.setUserProfiles(i.getUserProfiles());
                        responseFlag = userService.update(user);
                        break;
                    case DELETE:
                        responseFlag = userService.delete(userService.findByPrimaryKey(user.getId()));
                        break;
                }
                modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
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
                switch (oper) {
                    case ADD:
                        responseFlag = preAssyService.insert(preAssy);
                        break;
                    case EDIT:
                        responseFlag = preAssyService.update(preAssy);
                        break;
                    case DELETE:
                        responseFlag = preAssyService.delete(preAssyService.findByPrimaryKey(preAssy.getId()));
                        break;
                }
                modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                break;
            case "Type":
                switch (oper) {
                    case ADD:
                        responseFlag = typeService.insert(type);
                        break;
                    case EDIT:
                        responseFlag = typeService.update(type);
                        break;
                    case DELETE:
                        responseFlag = typeService.delete(typeService.findByPrimaryKey(type.getId()));
                        break;
                }
                modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
                break;
            default:
                modifyMessage = this.FAIL_MESSAGE;
        }

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

    private void showParams(HttpServletRequest req) throws ServletException, IOException {
        Enumeration params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + req.getParameter(paramName));
        }
    }

    private void encryptPassword(User user) {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        String encryptPassord = encoder.encode(user.getPassword());
        user.setPassword(encryptPassord);
    }
}

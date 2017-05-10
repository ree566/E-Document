/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import com.advantech.response.JqGridResponse;
import com.advantech.service.FlowGroupService;
import com.advantech.service.FlowService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/Flow")
public class FlowController extends CrudController<Flow> {

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowGroupService flowGroupService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse findAll(PageInfo info) {
        return toJqGridResponse(flowService.findAll(info), info);
    }
    
    @ResponseBody
    @RequestMapping(value = "/flowGroup", method = {RequestMethod.GET})
    protected List findFlowGroup() {
        return flowGroupService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(String oper, Flow flow) {
        String modifyMessage;
        int responseFlag;

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
                    Flow existFlow = flowService.findByPrimaryKey(flow.getId());
                    flow.setFlowsForBabFlowId(existFlow.getFlowsForBabFlowId());
                    flow.setFlowsForTestFlowId(existFlow.getFlowsForTestFlowId());
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

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = "/find_sub", method = {RequestMethod.GET})
    public List getFlowOptionByParent(
            @RequestParam int id,
            @ModelAttribute PageInfo info) {
        return flowService.findByParent(id);
    }

    @ResponseBody
    @RequestMapping(value = "/mod_sub", method = {RequestMethod.POST})
    protected ResponseEntity updateSubFlow(String oper, Flow flow, @RequestParam int parentFlowId) {
        String modifyMessage;
        int responseFlag;

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

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

}

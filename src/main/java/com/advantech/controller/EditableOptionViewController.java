/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.JsonHelper;
import com.advantech.helper.PageInfo;
import com.advantech.model.Identit;
import com.advantech.response.JqGridResponse;
import com.advantech.service.FlowService;
import com.advantech.service.IdentitService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private FlowService flowService;

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
    public JqGridResponse getSheetView(@PathVariable(value = "tableName") final String tableName, @ModelAttribute PageInfo info, @ModelAttribute("user") Identit user) throws JsonProcessingException, IOException {

        JqGridResponse viewResp;
        List l;

        switch (tableName) {
            case "Flow":
                l = flowService.findAll(info);
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
        String jsonString = JsonHelper.toStringWithLazyLoading(l);
        ObjectMapper mapper = new ObjectMapper();
        l = mapper.readValue(jsonString, ArrayList.class);

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

    @ExceptionHandler(HttpSessionRequiredException.class)
//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The session has expired")
    public ResponseEntity handleSessionExpired() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("The session has expired.");
    }

}

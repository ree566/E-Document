/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.excel.JxlsExcelView;
import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.jqgrid.JqGridResponse;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowPermutations;
import com.advantech.service.FlowPermutationsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wei.Cheng For controller test, can be removed.
 */
@Controller
@RequestMapping(value = "/FlowPermutationsController")
public class FlowPermutationsController extends CrudController<FlowPermutations> {

    @Autowired
    private FlowPermutationsService service;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse read(PageInfo info) {
        return toJqGridResponse(service.findAll(info), info);
    }

    @ResponseBody
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity insert(FlowPermutations pojo, BindingResult bindingResult) throws Exception {
        String modifyMessage = service.insert(pojo) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(FlowPermutations pojo, BindingResult bindingResult) throws Exception {
        String modifyMessage = service.update(pojo) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity delete(int id) throws Exception {
        String modifyMessage = service.delete(id) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }
    
    @ResponseBody
    @RequestMapping(value = "excel", method = {RequestMethod.GET})
    protected ModelAndView excel() throws Exception {
        List<FlowPermutations> l = service.findAll();
        Map<String, Object> model = new HashMap<>();
        model.put("data", l);
        String fileName = "flow_permutations.xls";
        return new ModelAndView(new JxlsExcelView("excel-template/" + fileName, fileName), model);
    }

}

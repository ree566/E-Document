/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.model.FqcModelStandardTime;
import com.advantech.service.FqcModelStandardTimeService;
import static com.google.common.base.Preconditions.checkArgument;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng Line login and logout function are Deprecated
 */
@Controller
@RequestMapping(value = "/FqcModelStandardTimeController")
public class FqcModelStandardTimeController {

    @Autowired
    private FqcModelStandardTimeService fqcModelStandardTimeService;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findAll() {
        return new DataTableResponse(fqcModelStandardTimeService.findAll());
    }

    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveOrUpdate(@Valid @ModelAttribute FqcModelStandardTime pojo) {
        checkSeriesName(pojo);
        if (pojo.getId() == 0) {
            fqcModelStandardTimeService.insert(pojo);
        } else {
            fqcModelStandardTimeService.update(pojo);
        }
        return "success";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@RequestParam int id) {
        FqcModelStandardTime pojo = fqcModelStandardTimeService.findByPrimaryKey(id);
        fqcModelStandardTimeService.delete(pojo);
        return "success";
    }
    
    private void checkSeriesName(FqcModelStandardTime pojo) {
        List<FqcModelStandardTime> l = fqcModelStandardTimeService.findByName(pojo.getModelNameCategory());
        if (!l.isEmpty()) {
            l.forEach(s -> {
                checkArgument(s.getId() == pojo.getId(), "Series standardTime is already exist");
            });
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.PageInfo;
import com.advantech.model.Model;
import com.advantech.model.SheetIe;
import com.advantech.model.SheetSpe;
import com.advantech.service.SheetViewService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class Test1 {

    @ResponseBody
    @RequestMapping(value = "/testInput", method = {RequestMethod.GET, RequestMethod.POST})
    public String testInput(@ModelAttribute Model model,
            @ModelAttribute SheetSpe spe,
            @ModelAttribute SheetIe ie,
            HttpServletResponse res) {
        List l = new ArrayList();
        l.add(model);
        l.add(spe);
        l.add(ie);
//        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return new Gson().toJson(l);
    }

    @ResponseBody
    @RequestMapping(value = "/getItem", method = {RequestMethod.GET, RequestMethod.POST})
    public String getItem(@ModelAttribute PageInfo info) throws IOException {
//        ModelService modelService = new ModelService();
//        SheetSPEService sheetSpeService = new SheetSPEService();
//        SheetEEService sheetEEService = new SheetEEService();
//        SheetIEService sheetIEService = new SheetIEService();
//        List l = new ArrayList();
//        l.add(modelService.findAll(info));
//        l.add(sheetSpeService.findAll(info));
//        l.add(sheetEEService.findAll(info));
//        l.add(sheetIEService.findAll(info));
        return toJsonString(new ArrayList());
    }

    @ResponseBody
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public PageInfo getInfo(@ModelAttribute PageInfo info) throws IOException {
        System.out.println("HomeController: Passing through...");
        System.out.println(new Gson().toJson(info));

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/lazyTest", method = RequestMethod.GET)
    public List lazyTest(@ModelAttribute PageInfo info) throws IOException {
        return (List) getTestObject(info);
    }

    private Object getTestObject(PageInfo info) throws IOException {
        System.out.println("HomeController: Passing through...");
        System.out.println(new Gson().toJson(info));
        SheetViewService service = new SheetViewService();
        return toJsonString(service.findAll(info));
    }

    private String toJsonString(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Hibernate4Module hbm = new Hibernate4Module();
        hbm.enable(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);

        mapper.registerModule(hbm);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter, obj);
        return stringWriter.toString();
    }
}

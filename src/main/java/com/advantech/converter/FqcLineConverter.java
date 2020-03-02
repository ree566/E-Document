package com.advantech.converter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.advantech.model.db1.FqcLine;
import com.advantech.service.db1.FqcLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class FqcLineConverter implements Converter<String, FqcLine> {

    @Autowired
    private FqcLineService fqcLineService;

    @Override
    public FqcLine convert(String s) {
        return fqcLineService.findByPrimaryKey(Integer.parseInt(s));
    }

}

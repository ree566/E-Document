package com.advantech.converter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.advantech.model.Line;
import com.advantech.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class BabLineConverter implements Converter<String, Line> {

    @Autowired
    private LineService lineService;

    @Override
    public Line convert(String s) {
        return lineService.findByPrimaryKey(Integer.parseInt(s));
    }

}

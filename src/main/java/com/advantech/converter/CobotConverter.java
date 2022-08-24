/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.Cobot;
import com.advantech.service.CobotService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class CobotConverter implements Converter<String, Cobot> {

    @Autowired
    private CobotService cobotService;

    @Override
    public Cobot convert(String s) {
        return cobotService.findByPrimaryKey(Integer.parseInt(s));
    }

    public String convertToString(Set<Cobot> cobots){
        String[] names = cobots.stream().map(c -> c.getName()).toArray(String[]::new);
        return String.join(",", names);
    }
}

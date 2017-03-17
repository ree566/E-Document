/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class Test1 {

    @RequestMapping(value = "/getItem", method = RequestMethod.GET)
    public String home() {
        System.out.println("HomeController: Passing through...");
        return "test.jsp";
    }
}

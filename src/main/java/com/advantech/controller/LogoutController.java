/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class LogoutController {

    @RequestMapping(value = "/logout.do", method = {RequestMethod.POST})
    public String logout(HttpSession session) {
        System.out.println("user logout");

        session.invalidate();
        return "redirect:/login.jsp";
    }
}

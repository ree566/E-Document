/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Identit;
import com.advantech.service.IdentitService;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@SessionAttributes(value = {"user"})
public class LoginController {

    @RequestMapping(value = "/login.do", method = {RequestMethod.POST})
    public String login(@RequestParam String jobnumber, @RequestParam String password, Model model, HttpSession session) {
        System.out.println("user login");
        IdentitService service = new IdentitService();
        Identit i = service.findByJobnumber(jobnumber);
        if (i == null || !i.getPassword().equals(password)) {
            System.out.println("user not found");
            model.addAttribute("errormsg", "錯誤的帳號或密碼");
            return "forward:login.jsp";
        } else {
            //Initialize the lazy loading relative object
            Hibernate.initialize(i.getUserType());
            Hibernate.initialize(i.getFloor());
            model.addAttribute("user", i);
            return "redirect:pages/index.jsp";
        }
    }

    @RequestMapping(value = "/logout.do", method = {RequestMethod.POST})
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.jsp";
    }
}

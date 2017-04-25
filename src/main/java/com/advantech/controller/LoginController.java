/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import static com.advantech.helper.PasswordEncoder.isPasswordHashMatches;
import com.advantech.model.Identit;
import com.advantech.service.IdentitService;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IdentitService service;

    @RequestMapping(value = "/login.do", method = {RequestMethod.POST})
    public String login(@RequestParam String jobnumber, @RequestParam String password, Model model, HttpSession session) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("user login");
        Identit i = service.findByJobnumber(jobnumber);
        if (i == null || !isPasswordHashMatches(password, i.getPassword())) {
            System.out.println("user not found");
            model.addAttribute("errormsg", "錯誤的帳號或密碼");
            return "forward:login.jsp";
        } else {
            model.addAttribute("user", i);
            return "redirect:pages/index.jsp";
        }
    }
    
}

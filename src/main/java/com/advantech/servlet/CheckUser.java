/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查看所屬人員是否存在
 */
package com.advantech.servlet;

import com.advantech.entity.Identit;
import com.advantech.helper.ParamChecker;
import com.advantech.service.IdentitService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class CheckUser {

    @Autowired
    private IdentitService identitService;

    @Autowired
    private ParamChecker pChecker;

    @RequestMapping(value = "/CheckUser", method = {RequestMethod.POST})
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String jobnumber = req.getParameter("jobnumber");
//        String password = req.getParameter("password");

        out.print(pChecker.checkInputVals(jobnumber) ? isUserExist(jobnumber) : false);
    }

    private boolean isUserExist(String jobnumber) {
        //change the sql query(password not check)
        Identit i = identitService.getIdentit(jobnumber);
        return !(i == null);
    }

}

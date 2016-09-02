package com.advantech.servlet;

import com.advantech.entity.Identit;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CheckUser", urlPatterns = {"/CheckUser"})
public class CheckUser extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private IdentitService identitService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init() throws ServletException {
        identitService = BasicService.getIdentitService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        doPost(req, res);
    }

    @Override
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

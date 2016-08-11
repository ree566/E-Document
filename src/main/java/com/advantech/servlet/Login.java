package com.advantech.servlet;

import com.advantech.entity.Identit;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/Login"},
        initParams = {
            @WebInitParam(name = "SUCCESS", value = "index.jsp"),
            @WebInitParam(name = "FAIL", value = "login.jsp")
        }
)
public class Login extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String SUCCESS;
    private String FAIL;

    private IdentitService identitService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init() throws ServletException {
        SUCCESS = getServletConfig().getInitParameter("SUCCESS");
        FAIL = getServletConfig().getInitParameter("FAIL");
        identitService = BasicService.getIdentitService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        HttpSession session = req.getSession(false);
//        if (session != null && session.getAttribute("jobnumber") != null) {
//            res.sendRedirect(SUCCESS);
//        } else {
//            req.getRequestDispatcher(FAIL).forward(req, res);
//        }
        doPost(req, res);
    }

    @Override
    @SuppressWarnings("null")
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        String jobnumber = req.getParameter("jobnumber");
        String password = req.getParameter("password");

        Identit i = checkInputValAndLogin(jobnumber, password);
        if (i != null) {
            session.setAttribute("id", i.getId());
            session.setAttribute("jobnumber", i.getJobnumber());
            session.setAttribute("name", i.getName());
            session.setAttribute("permission", i.getPermission());
            session.setAttribute("sitefloor", i.getSitefloor());
            res.sendRedirect(SUCCESS);
        } else {
            req.setAttribute("errormsg", "錯誤的帳號或密碼");
            req.getRequestDispatcher(FAIL).forward(req, res);
        }
    }

    private Identit checkInputValAndLogin(String jobnumber, String password) {
//        boolean isParamVaild = pChecker.checkInputVals(jobnumber, password);
//        if (!isParamVaild) {
//            return null;
//        }
        //change the sql query
        return identitService.getIdentit(jobnumber, password);
      
       
    }

}

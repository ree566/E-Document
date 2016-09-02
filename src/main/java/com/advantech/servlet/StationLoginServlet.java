/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢組裝包裝相關資訊(工單對照的機種、線別資訊等用)
 */
package com.advantech.servlet;

import com.advantech.entity.BAB;
import com.advantech.entity.BABLoginStatus;
import com.advantech.entity.Line;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BABLoginStatusService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "StationLoginServlet", urlPatterns = {"/StationLoginServlet"})
public class StationLoginServlet extends HttpServlet {

    private BABLoginStatusService blsService = null;
    private final String TESTLINE = "testLine", BABLINE = "babLine", LOGIN = "LOGIN", LOGOUT = "LOGOUT", CHANGEUSER = "CHANGEUSER";
    private ParamChecker pChecker;

    @Override
    public void init()
            throws ServletException {
        blsService = BasicService.getBabLoginStatusService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String lineType = req.getParameter("lineType");
        String action = req.getParameter("action");

        String unSupportAction = "Unsupport action.";

        if (pChecker.checkInputVals(lineType, action)) {
            switch (lineType) {
                case TESTLINE:
                    out.print(TESTLINE + " called, but action not support.");
                    break;
                case BABLINE:
                    String lineNo = req.getParameter("lineNo");
                    String jobnumber = req.getParameter("jobnumber");
                    String station = req.getParameter("station");
                    if (pChecker.checkInputVals(lineNo, jobnumber, station)) {

                        int line = Integer.parseInt(lineNo);
                        int st = Integer.parseInt(station);

                        BABLoginStatus user = blsService.getUser(jobnumber);
                        BABLoginStatus bStatus = blsService.getBABLoginStatus(line, st);

                        switch (action) {
                            case LOGIN:
                                if (bStatus != null) {
                                    out.print("此站別正在被使用者 " + bStatus.getJobnumber() + " 使用");
                                } else if (user != null) {
                                    Line l = BasicService.getLineService().getLine(user.getLineId());
                                    out.print("您已經在線別 " + l.getName() + "的站別 " + user.getStation() + " 登入");
                                } else if (st == 1) {
                                    out.print(blsService.firstStationBABLogin(line, jobnumber) ? "success" : "Fail to login user");
                                } else {
                                    out.print(blsService.babLogin(line, st, jobnumber) ? "success" : "Fail to login user");
                                }
                                break;
                            case LOGOUT:
                                if (bStatus != null) {
                                    if (st == 1) {
                                        out.print(blsService.firstStationBABLogout(line) ? "success" : "Fail to logout user");
                                    } else {
                                        out.print(blsService.deleteUserFromStation(line, st) ? "success" : "Fail to logout user");
                                    }
                                } else {
                                    out.print("查無使用者，無法登出");
                                }
                                break;
                            case CHANGEUSER:
                                if (bStatus == null) {
                                    out.print("無法換人，此站別人員不存在");
                                } else if (blsService.changeUser(line, st, jobnumber)) {
                                    BAB b = BasicService.getBabService().getLastInputBAB(line);
                                    if (b != null) {
                                        blsService.recordBABPeople(b.getId(), st, jobnumber);
                                    }
                                    out.print("success");
                                } else {
                                    out.print("Fail to change user");
                                }
                                break;
                            default:
                                out.print(unSupportAction);
                        }
                    } else {
                        out.print("Invaild input vals.");
                    }
                    break;
                default:
                    out.print(unSupportAction);
                    break;
            }
        } else {
            out.print(unSupportAction);
        }
    }
}

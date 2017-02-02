/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 顯示看板XML是否有資料用 與其他class無相依關係(開看板刷新時用，若無需求可刪除此servlet)
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetLineBalancingComparison", urlPatterns = {"/GetLineBalancingComparison"})
public class GetLineBalancingComparison extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(GetLineBalancingComparison.class);
    private BABService babService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        babService = BasicService.getBabService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String type = req.getParameter("type");

        String Model_name = req.getParameter("Model_name");
        String lineType = req.getParameter("lineType");
        String BABid = req.getParameter("BABid");

        JSONObject responseObject = new JSONObject();

        List<Map> l = null;

        if (pChecker.checkInputVal(type)) {

            if (null != type) {
                switch (type) {
                    case "type1":
                        l = babService.getLineBalanceCompare(Model_name, lineType);
                        break;
                    case "type2":
                        l = babService.getLineBalanceCompare(Integer.parseInt(BABid));
                        break;
                    default:
                        l = new ArrayList();
                        break;
                }
            }

            try {
                if (l != null) {
                    for (Map m : l) {
                        int ctrl_isused = parseToInt(m.get("ctrl_isused"));
                        int ctrl_id = parseToInt(m.get("ctrl_id"));
                        int exp_isused = parseToInt(m.get("exp_isused"));
                        int exp_id = parseToInt(m.get("exp_id"));

                        Double ctrlBalance = ctrl_id == 0 ? 0 : babService.getAvgType2(ctrl_id, ctrl_isused);
                        Double expBalance = exp_id == 0 ? 0 : babService.getAvgType2(exp_id, exp_isused);

                        responseObject.put("ctrlAvgs", ctrlBalance).put("expAvgs", expBalance);

                    }
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
        } else {
            l = new ArrayList();
        }

        responseObject.put("data", l);
        out.print(responseObject);
    }

    private int parseToInt(Object o) {
        return o == null ? 0 : (int) o;
    }
}

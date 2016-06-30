/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 儲存資料到LS_BAB用
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.entity.BAB;
import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import java.io.*;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "SaveBABInfo", urlPatterns = {"/SaveBABInfo"})
public class SaveBABInfo extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SaveBABInfo.class);
    private BABService babService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init() throws ServletException {
        babService = BasicService.getBabService();
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
        String po = req.getParameter("po");
        String modelName = req.getParameter("modelname");
        String line = req.getParameter("line");
        String people = req.getParameter("people");
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        JSONObject serverMsg = null;
        if (pChecker.checkInputVals(po, modelName, line, people)) {
            try {
                int lineNo = Integer.parseInt(line);
                serverMsg = new JSONObject();
                if (type.equals("1")) {
                    BAB bab = new BAB(po, modelName, lineNo, Integer.parseInt(people));
                    String str = babService.checkAndStartBAB(bab);
                    if ("success".equals(str)) {
                        serverMsg = babService.getProcessingBABByLine(lineNo);
                        serverMsg.put("status", "success");
                        sendMailAfterBABRunIn(bab);
                    } else {
                        serverMsg = new JSONObject().put("status", str);
                    }
                } else {
                    String str = babService.closeBAB(id);
                    serverMsg = new JSONObject().put("status", !"".equals(str) ? str : "錯誤");
                }

            } catch (JSONException | NumberFormatException e) {
                log.error(e.toString());
            } catch (Exception ex) {
                log.error(ex.toString());
            } finally {
                out.print(serverMsg);
            }
        }
    }

    private void sendMailAfterBABRunIn(BAB bab) throws MessagingException {

        String targetMail = PropertiesReader.getInstance().getTestMail();
        if ("".equals(targetMail)) {
            return;
        }

        String subject = "[藍燈系統]系統訊息";
        MailSend.getInstance().sendMailWithoutSender(this.getClass(), targetMail, subject, generateMailBody(bab));

    }

    private String generateMailBody(BAB bab) {
        return new StringBuilder()
                .append("<p>現在時間 <strong>")
                .append(getToday())
                .append("</strong> </p>")
                .append("<p>系統開始測量線平衡與蒐集資料</p>")
                .append("<p>工單號碼: ")
                .append(bab.getPO())
                .append("</p><p>生產機種: ")
                .append(bab.getModel_name())
                .append("</p><p>生產人數: ")
                .append(bab.getPeople())
                .append("</p><p>線別號碼: ")
                .append(bab.getLine())
                .append("</p><p>詳細歷史資料請上 <a href='")
                .append("//172.20.131.52:8080/CalculatorWSApplication/BabTotal")
                .append("'>線平衡電子化系統</a> 中的歷史紀錄做查詢</p>")
                .toString();
    }

    private String getToday() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").print(new DateTime());
    }
}

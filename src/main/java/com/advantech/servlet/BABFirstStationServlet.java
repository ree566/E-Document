/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 儲存資料到LS_BAB用，負責第一站投工單
 */
package com.advantech.servlet;

import com.advantech.endpoint.Endpoint6;
import com.advantech.entity.Bab;
import com.advantech.helper.MailSend;
import com.advantech.helper.ParamChecker;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BabService;
import java.io.*;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class BABFirstStationServlet {
    
    private static final Logger log = LoggerFactory.getLogger(BABFirstStationServlet.class);
    
    @Autowired
    private BabService babService;
    
    @Autowired
    private ParamChecker pChecker;
   
    @RequestMapping(value = "/BABFirstStationServlet", method = {RequestMethod.POST})
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        
        String po = req.getParameter("po");
        String modelName = req.getParameter("modelname");
        String line = req.getParameter("lineNo");
        String people = req.getParameter("people");
        String startPosition = req.getParameter("startPosition");
        String ispre = req.getParameter("ispre");
        
        String jobnumber = req.getParameter("jobnumber");
        
        String successMessage = "success";
        String result = "";
        
        if (pChecker.checkInputVals(po, modelName, line, people, jobnumber, startPosition, ispre)) {
            try {
                int lineNo = Integer.parseInt(line);
                Bab bab = new Bab(
                        po,
                        modelName,
                        lineNo,
                        Integer.parseInt(people),
                        Integer.parseInt(startPosition),
                        Integer.parseInt(ispre)
                );
                result = babService.checkAndStartBAB(bab, jobnumber);
                sendMailAfterBABRunIn(bab);
            } catch (Exception e) {
                log.error(e.toString());
            } finally {
                if (successMessage.equals(result)) {
                    Endpoint6.syncAndEcho();
                }
                out.print(result);
            }
        } else {
            out.print("Invaild input value");
        }
    }
    
    private void sendMailAfterBABRunIn(Bab bab) throws MessagingException {
        
        String targetMail = PropertiesReader.getInstance().getTestMail();
        if ("".equals(targetMail)) {
            return;
        }
        
        String subject = "[藍燈系統]系統訊息";
        MailSend.getInstance().sendMail(targetMail, subject, generateMailBody(bab));
        
    }
    
    private String generateMailBody(Bab bab) {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BasicService;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class CountermeasureAlarm implements Job {

    private static final Logger log = LoggerFactory.getLogger(CountermeasureAlarm.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            //Link the mailloop to send daily mail
            sendMail();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void sendMail() throws Exception {
        PropertiesReader p = PropertiesReader.getInstance();
        JSONArray mailTarget = p.getTargetMailLoop();
        JSONArray ccMailLoop = p.getTargetCCLoop().getJSONArray("mailloop");
        String subject = "[藍燈系統]未填寫異常回覆工單列表 ";

        for (int i = 0; i < mailTarget.length(); i++) {
            JSONObject targetInfo = (JSONObject) mailTarget.get(i);
            String sitefloor = (String) targetInfo.get("sitefloor");
            JSONArray mailLoop = (JSONArray) targetInfo.get("mailloop");
            String mailBody = this.generateMailBody(sitefloor);
            if (!"".equals(mailBody)) { //有資料再寄信
                MailSend.getInstance().sendMail(mailLoop, ccMailLoop, subject + sitefloor + "F", mailBody);
            }
        }
    }

    public String generateMailBody(String sitefloor) {
        List<Map> l = BasicService.getCountermeasureService().getUnFillCountermeasureBabs(sitefloor);
        if (l.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("<style>table {border-collapse: collapse;} table, th, td {border: 1px solid black; padding: 5px;}</style>");
            sb.append("<p>Dear 使用者:</p>");
            sb.append("<p>以下是亮燈頻率高於基準值，尚未回覆異常原因的工單列表</p>");
            sb.append("<p>請抽空至 藍燈系統 > 線平衡資料查詢頁面 > 檢視詳細 填寫相關異常因素，謝謝</p>");
            sb.append("<table>");
            sb.append("<tr><th>製程</th><th>線別</th><th>工單</th><th>機種</th><th>投入時間</th></tr>");
            for (Map m : l) {
                sb.append("<tr><td>")
                        .append(m.get("linetype"))
                        .append("</td><td>")
                        .append(m.get("lineName"))
                        .append("</td><td>")
                        .append(m.get("PO"))
                        .append("</td><td>")
                        .append(m.get("Model_name"))
                        .append("</td><td>")
                        .append(m.get("Btime"))
                        .append("</td></tr>");
            }
            sb.append("</table>");
            sb.append("<p>資料共計: ");
            sb.append(l.size());
            sb.append(" 筆</p>");
            return sb.toString();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BasicService;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final DecimalFormat formatter = new DecimalFormat("#.##%");
    private final JSONObject responseUserPerLine = PropertiesReader.getInstance().getResponseUserPerLine();
    private final DatetimeGenerator dg = new DatetimeGenerator("yyyy-MM-dd HH:mm");

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
            Set<String> highlightLines = new HashSet();
            StringBuilder sb = new StringBuilder();
            sb.append("<style>table {border-collapse: collapse;} table, th, td {border: 1px solid black; padding: 5px;}</style>");
            sb.append("<p>Dear 使用者:</p>");
            sb.append("<p>以下是亮燈頻率高於基準值，尚未回覆異常原因的工單列表</p>");
            sb.append("<p>請於 <mark><strong style='color: red'>今日下班前</strong></mark> 至 藍燈系統 > 線平衡資料查詢頁面 > 檢視詳細 填寫工單異常因素，謝謝</p>");
            sb.append("<table>");
            sb.append("<tr><th>製程</th><th>線別</th><th>工單</th><th>機種</th><th>亮燈頻率</th><th>數量</th><th>投入時間</th></tr>");
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
                        .append(formatter.format(m.get("almPercent")))
                        .append("</td><td>")
                        .append(m.get("qty"))
                        .append("</td><td>")
                        .append(dg.dateFormatToString(m.get("Btime")))
                        .append("</td></tr>");
                highlightLines.add((String) m.get("lineName"));
            }
            sb.append("</table>");
            sb.append("<p>線別負責人: </p>");

            for (String line : highlightLines) {
                sb.append("<p>");
                sb.append(line);
                sb.append(" : ");
                JSONArray arr = responseUserPerLine.getJSONArray(line.trim());
                for (int i = 0, j = arr.length(); i < j; i++) {
                    sb.append(arr.get(i));
                    if (i != j - 1) {
                        sb.append("、");
                    }
                }
                sb.append("</p>");
            }

            sb.append("<p>資料共計: ");
            sb.append(l.size());
            sb.append(" 筆</p>");
            return sb.toString();
        }
    }
}

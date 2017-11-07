/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Bab;
import com.advantech.entity.Line;
import com.advantech.entity.LineBalancing;
import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.LineBalancingDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class LineBalanceService {

    private static final Logger log = LoggerFactory.getLogger(LineBalancingDAO.class);

    private String targetMail;
    private int BALANCE_ROUNDING_DIGIT;
    
    @Autowired
    private LineBalancingDAO lineBalanceDAO;
    
    @Autowired
    private LineService lineService;

    @PostConstruct
    protected void init() {
        PropertiesReader p = PropertiesReader.getInstance();
        targetMail = p.getTestMail();
        BALANCE_ROUNDING_DIGIT = p.getBalanceRoundingDigit();
    }

    public LineBalancing getMaxBalance(Bab bab) {
        return lineBalanceDAO.getMaxBalance(bab);
    }

    public Double caculateLineBalance(JSONArray balances) throws JSONException {

        double max = 0.0;
        double sum = 0.0;
        double balancing = -1;

        if (balances != null) {
            int babPeople = balances.length();
            for (int a = 0; a < babPeople; a++) {//Find the max avg and sum the avgs.
                double avg = balances.getJSONObject(a).getInt("average");
                if (max < avg) {
                    max = avg;
                }
                sum += avg;
            }
            balancing = (max != 0 ? caculateLineBalance(max, sum, babPeople) : 0.0);
        }
        return balancing;
    }

    public Double caculateLineBalance(Double max, Double sum, int people) {
        return new BigDecimal(sum / (max * people))
                .setScale(BALANCE_ROUNDING_DIGIT, BigDecimal.ROUND_HALF_DOWN)
                .doubleValue();
    }

    public void checkLineBalanceAndSendMail(Bab bab, LineBalancing linebaln, double balance) throws JSONException, MessagingException {
        double balnDiff = PropertiesReader.getInstance().getBalanceDiff();
        //確定資料庫已經插入之後才送信
        int maxbln = parseDoubleToInteger(linebaln == null ? 0 : linebaln.getBalance());
        int currentbln = parseDoubleToInteger(balance);
        int diff = parseDoubleToInteger(balnDiff);
        if ((maxbln - currentbln) > diff) {
            sendMail(bab, maxbln, currentbln, diff);
            log.info("begin send mail because diff = "
                    + diff
                    + " and the current linebalance is "
                    + currentbln
                    + " ,the maxium in this line is "
                    + maxbln
            );
        }
    }

    private int parseDoubleToInteger(double d) {
        return (int) Math.round(d * 100);
    }

    private void sendMail(Bab bab, int num1, int num2, int diff) throws JSONException, MessagingException {
        Line line = lineService.getLine(bab.getLine());
        String mailto = targetMail; //Get the responsor of linetype.
        if ("".equals(mailto)) {
            return;
        }
        String subject = "[藍燈系統]異常訊息(" + line.getName().trim() + ")";
        MailSend.getInstance().sendMail(mailto, subject,
                new StringBuilder()
                        .append("<p>時間 <strong>")
                        .append(new Date())
                        .append("</strong> 統計到的線平衡率</p>")
                        .append("<p>與資料庫儲存的最佳平衡比對，下降差距到達了")
                        .append("百分之 ")
                        .append(diff)
                        .append(" </p><p>")
                        .append(num1)
                        .append("% ----> <font style='color:red'>")
                        .append(num2)
                        .append("</font>%</p>")
                        .append("<p>工單號碼: ")
                        .append(bab.getPO())
                        .append("</p><p>生產機種: ")
                        .append(bab.getModel_name())
                        .append("</p><p>生產人數: ")
                        .append(bab.getPeople())
                        .append("</p><p>詳細歷史資料請上 <a href='")
                        .append("http://172.20.131.208/Line_Balancing/Login.aspx")
                        .append("'>線平衡電子化系統</a> 中的歷史紀錄做查詢</p>")
                        .toString());
    }
}

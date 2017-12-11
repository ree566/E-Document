/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Bab;
import com.advantech.model.Line;
import com.advantech.model.LineBalancing;
import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.dao.LineBalancingDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.model.view.BabAvg;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional("transactionManager2")
public class LineBalancingService {

    private static final Logger log = LoggerFactory.getLogger(LineBalancingDAO.class);

    private String targetMail;
    private int BALANCE_ROUNDING_DIGIT;

    @Autowired
    private LineBalancingDAO lineBalanceDAO;

    @Autowired
    private SqlViewDAO sqlViewDAO;

    @PostConstruct
    protected void init() {
        PropertiesReader p = PropertiesReader.getInstance();
        targetMail = p.getTestMail();
        BALANCE_ROUNDING_DIGIT = p.getBalanceRoundingDigit();
    }

    public List<LineBalancing> findAll() {
        return lineBalanceDAO.findAll();
    }

    public LineBalancing findByPrimaryKey(Object obj_id) {
        return lineBalanceDAO.findByPrimaryKey(obj_id);
    }

    public LineBalancing getMaxBalance(Bab bab) {
        return lineBalanceDAO.getMaxBalance(bab);
    }

    public int insert(Bab bab) {
        List<BabAvg> balances = sqlViewDAO.findBabAvg(bab.getId());

        LineBalancing maxBaln = this.getMaxBalance(bab); //先取得max才insert，不然會抓到自己
        double baln = this.caculateLineBalance(balances);

        LineBalancing record = new LineBalancing(
                bab.getPeople(),
                bab.getLine().getLineType().getName(),
                bab.getPo(),
                bab.getModelName(),
                Integer.toString(bab.getLine().getId())
        );

        record.setBalance(baln);

        try {
            for (int i = 0; i < balances.size(); i++) {
                Double avg = balances.get(i).getAverage();
                BeanUtils.setProperty(record, ("avg" + (i + 1)), avg);
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        lineBalanceDAO.insert(record);
        
        //Don't throw exception when mail send fail
        try {
            this.checkLineBalanceAndSendMail(bab, maxBaln, baln);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 1;
    }

    public int update(LineBalancing pojo) {
        return lineBalanceDAO.update(pojo);
    }

    public int delete(LineBalancing pojo) {
        return lineBalanceDAO.delete(pojo);
    }

    public Double caculateLineBalance(List<BabAvg> balances) throws JSONException {

        double max = 0.0;
        double sum = 0.0;
        double balancing = -1;

        if (balances != null && !balances.isEmpty()) {
            int babPeople = balances.size();
            for (int a = 0; a < babPeople; a++) {//Find the max avg and sum the avgs.
                double avg = balances.get(a).getAverage();
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

    private void sendMail(Bab bab, int maxbln, int currentbln, int diff) throws JSONException, MessagingException {
        Line line = bab.getLine();
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
                        .append(maxbln)
                        .append("% ----> <font style='color:red'>")
                        .append(currentbln)
                        .append("</font>%</p>")
                        .append("<p>工單號碼: ")
                        .append(bab.getPo())
                        .append("</p><p>生產機種: ")
                        .append(bab.getModelName())
                        .append("</p><p>生產人數: ")
                        .append(bab.getPeople())
                        .append("</p><p>詳細歷史資料請上 <a href='")
                        .append("http://172.20.131.208/Line_Balancing/Login.aspx")
                        .append("'>線平衡電子化系統</a> 中的歷史紀錄做查詢</p>")
                        .toString());
    }

}

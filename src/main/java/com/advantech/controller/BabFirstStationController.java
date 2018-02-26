/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 儲存資料到LS_BAB用，負責第一站投工單
 */
package com.advantech.controller;

import com.advantech.helper.MailManager;
import com.advantech.model.Bab;
import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.Line;
import com.advantech.model.TagNameComparison;
import com.advantech.model.User;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabService;
import com.advantech.service.TagNameComparisonService;
import com.advantech.service.UserService;
import static com.google.common.base.Preconditions.*;
import java.util.List;
import java.util.Objects;
import javax.mail.MessagingException;
import javax.validation.Valid;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabFirstStationController")
public class BabFirstStationController {

    private static final Logger log = LoggerFactory.getLogger(BabFirstStationController.class);

    private final String notify_name = "bab_run_in_alarm";

    @Autowired
    private BabService babService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private MailManager mailManager;

    @Autowired
    private UserService userService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @ResponseBody
    protected String insert(
            @Valid @ModelAttribute Bab bab,
            @RequestParam String tagName,
            @RequestParam String jobnumber,
            @RequestParam(required = false) String recordLineType
    ) {

        checkArgument(!(bab.getIspre() != 1 && bab.getPeople() == 1), "非前置工單人數不可為1");

        BabSensorLoginRecord loginRecord = babSensorLoginRecordService.findBySensor(tagName);
        checkArgument(loginRecord != null, "Can't find login record on this tagName");
        checkArgument(Objects.equals(loginRecord.getJobnumber(), jobnumber), "Jobnumber is not matches at loginRecord in database");

        TagNameComparison tag = tagNameComparisonService.findByLampSysTagName(tagName);
        checkArgument(tag != null, "Can't find tagName " + tagName + " in setting.");

        Line line = tag.getLine();

        checkArgument(bab.getPeople() + tag.getPosition() - 1 <= line.getPeople(),
                "People out of index(" + line.getPeople() + ")");

        bab.setLine(line);
        babService.checkAndInsert(bab, tag, recordLineType);

        //Don't show mail send error message when mail error caused.
        try {
            sendMailAfterBABRunIn(bab);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
//                Endpoint6.syncAndEcho();
        return "success";
    }

    private void sendMailAfterBABRunIn(Bab bab) throws MessagingException {

        List<User> l = userService.findByUserNotification(notify_name);

        String subject = "[藍燈系統]系統訊息";
        mailManager.sendMail(l, null, subject, generateMailBody(bab));

    }

    private String generateMailBody(Bab bab) {
        return new StringBuilder()
                .append("<p>現在時間 <strong>")
                .append(getToday())
                .append("</strong> </p>")
                .append("<p>系統開始測量線平衡與蒐集資料</p>")
                .append("<p>工單號碼: ")
                .append(bab.getPo())
                .append("</p><p>生產機種: ")
                .append(bab.getModelName())
                .append("</p><p>生產人數: ")
                .append(bab.getPeople())
                .append("</p><p>線別號碼: ")
                .append(bab.getLine().getName())
                .append("</p><p>詳細歷史資料請上 <a href='")
                .append("//172.20.131.52:8080/CalculatorWSApplication/BabTotal")
                .append("'>線平衡電子化系統</a> 中的歷史紀錄做查詢</p>")
                .toString();
    }

    private String getToday() {
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").print(new DateTime());
    }
}

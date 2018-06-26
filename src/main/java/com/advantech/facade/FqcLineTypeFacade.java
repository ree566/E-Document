/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.facade;

import com.advantech.model.Fqc;
import com.advantech.model.FqcLine;
import com.advantech.model.FqcModelStandardTime;
import com.advantech.model.FqcSettingHistory;
import com.advantech.model.LineTypeConfig;
import com.advantech.model.PassStationRecord;
import com.advantech.service.FqcLineService;
import com.advantech.service.FqcModelStandardTimeService;
import com.advantech.service.FqcProductivityHistoryService;
import com.advantech.service.FqcService;
import com.advantech.service.FqcSettingHistoryService;
import com.advantech.service.LineTypeConfigService;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.collect.Maps.newHashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class FqcLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(FqcLineTypeFacade.class);

    @Autowired
    private FqcService fqcService;

    @Autowired
    private FqcLineService fqcLineService;

    @Autowired
    private FqcModelStandardTimeService standardTimeService;

    @Autowired
    private FqcProductivityHistoryService fqcProducitvityHistoryService;

    @Autowired
    private FqcSettingHistoryService settingHistoryService;

    @Autowired
    private LineTypeConfigService lineTypeConfigService;

    @Autowired
    private WebServiceRV rv;

    private Double productivityMin, productivityMax;

    @PostConstruct
    protected void init() {
//        this.initMap();
        List<LineTypeConfig> l = lineTypeConfigService.findByLineType(5);
        LineTypeConfig conf = l.stream().filter(c -> "PRODUCTIVITY_STANDARD_MIN".equals(c.getName())).findFirst().orElse(null);
        productivityMin = conf == null ? 0.0 : conf.getValue().doubleValue();
        conf = l.stream().filter(c -> "PRODUCTIVITY_STANDARD_MAX".equals(c.getName())).findFirst().orElse(null);
        productivityMax = conf == null ? 0.0 : conf.getValue().doubleValue();
    }

    @Override
    protected void initMap() {
        this.dataMap.clear();
        List<FqcLine> l = fqcLineService.findAll();
        l.forEach((line) -> {
            dataMap.put(line.getName(), super.NORMAL_SIGN);
        });
    }

    /*
        Find all processing fqc with user info
        Get webService FQC_RI count and calc user's productivity
     */
    @Override
    public boolean generateData() {
        processingJsonObject = new JSONObject();
        boolean isSomeoneUnderStandard = false;

        List<Fqc> l = fqcService.findProcessingWithLine();
        List<Map<String, Object>> result = new ArrayList();

        List<FqcModelStandardTime> standardTimes = standardTimeService.findAll();
        Date now = new Date();

        //找進行的FQC, 抓標工, 抓MES台數, 抓進行時間(btime-now), 算效率
        for (Fqc fqc : l) {
            FqcSettingHistory history = settingHistoryService.findByFqc(fqc);
            FqcLine line = fqc.getFqcLine();

            FqcModelStandardTime standardTime = standardTimes.stream()
                    .filter(f -> fqc.getModelName().contains(f.getModelNameCategory()))
                    .max(Comparator.comparing(s -> s.getModelNameCategory().length()))
                    .orElse(null);

            List<PassStationRecord> records = rv.getPassStationRecords(fqc.getPo(), line.getFactory());
            records.removeIf(rec -> !history.getJobnumber().equals(rec.getUserNo())
                    || rec.getCreateDate().before(fqc.getBeginTime())
                    || rec.getCreateDate().after(now));

            long seconds = (now.getTime() - fqc.getBeginTime().getTime()) / 1000;

            Map<String, Object> info = newHashMap();
            info.put("fqc", fqc.getModelName());
            info.put("fqcLineName", line.getName());
            info.put("setting", history.getJobnumber());
            info.put("standardTime", standardTime == null ? 0 : standardTime.getStandardTime());
            info.put("records", records.size());
            info.put("seconds", seconds);

            Double productivity = (standardTime == null || (int) seconds == 0 || records.isEmpty()) ? 0
                    : calculateProductivity(standardTime.getStandardTime(), (int) seconds, records.size());
            boolean isPass = productivity <= productivityMax || productivity >= productivityMin;
            info.put("productivity", productivity);
            info.put("isPass", isPass ? 1 : 0);
//            
            result.add(info);

            isSomeoneUnderStandard = true;
        }
        processingJsonObject.put("data", result);

        return false;

    }

    /*
            檢驗效率 = 標準工時 * N台 / (結束 - 開始) * N台
            N台 = 幾個FQC RI
     */
    private Double calculateProductivity(int standardTime, int firstPcsTimeCost, int currentPcs) {
        Double producitvity = ((double) standardTime * (double) currentPcs) / ((double) firstPcsTimeCost * (double) currentPcs);
        return producitvity;
    }

    @Override
    public void initAlarmSign() {
//        List l = almService.findAll();
//        almService.delete(l);
//        almService.insert(this.mapToAlarmSign(dataMap));
    }

    @Override
    public void setAlarmSign(List l) {
//        almService.update(l);
    }

    @Override
    public void resetAlarmSign() {
//        almService.reset();
    }

    @Override
    public void setAlarmSignToTestingMode() {
//        almService.AlarmToTestingMode();
    }

    @Override
    protected List mapToAlarmSign(Map map) {
        return null;
    }

}

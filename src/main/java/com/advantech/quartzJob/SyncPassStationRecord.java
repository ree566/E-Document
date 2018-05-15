/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 此job的相關設定在CellScheduleJobServlet中，由此servlet分派子工作(本class)對webservice監聽並save data into database
 * 插入從WebService取得的過站資料job
 */
package com.advantech.quartzJob;

import com.advantech.model.PassStationRecord;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.model.Bab;
import com.advantech.model.LineType;
import com.advantech.service.PassStationRecordService;
import com.advantech.webservice.WebServiceRV;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class SyncPassStationRecord extends QuartzJobBean {

    private Bab bab;

    private String jobnumber;

    private final PassStationRecordService passStationService;

    private WebServiceRV rv;

    public SyncPassStationRecord() {
        passStationService = (PassStationRecordService) ApplicationContextHelper.getBean("passStationService");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        syncMesDataToDatabase();
    }

    //讓user自行開收工單，以免造成資料庫已關閉前端卻尚未關閉的情況
    private void syncMesDataToDatabase() {
        String po = bab.getPo();
        LineType lt = bab.getLine().getLineType();
        System.out.println("Begin check po:" + po + " / type:" + lt.getName());
        checkDifferenceAndInsert(po);
    }

    private void checkDifferenceAndInsert(String po) {
        
        List<PassStationRecord> history = passStationService.findByPo(po);
        List<PassStationRecord> l = rv.getPassStationRecords(po);
        
        history.removeIf(f -> !jobnumber.equals(f.getUserNo()));
        l.removeIf(f2 -> !jobnumber.equals(f2.getUserNo()));
       
        List<PassStationRecord> newData = (List<PassStationRecord>) CollectionUtils.subtract(l, history);

        if (!newData.isEmpty()) {
            passStationService.insert(newData);
        }
    }

    public void setBab(Bab bab) {
        this.bab = bab;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

}

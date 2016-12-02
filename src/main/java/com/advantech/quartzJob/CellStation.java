/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.PassStation;
import com.advantech.helper.CronTrigMod;
import com.advantech.service.BasicService;
import com.advantech.webservice.WebServiceRV;
import static java.lang.System.out;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

/**
 *
 * @author Wei.Cheng
 */
public class CellStation implements Job {

    private String currentPO;
    private Integer currentLineId;
    private JobKey currentJobKey;
    private TriggerKey currentTriggerKey;
    private String today;
    
    

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        this.currentPO = (String) dataMap.get("PO");
        this.currentLineId = (Integer) dataMap.get("LineId");
        this.currentTriggerKey = jec.getTrigger().getKey();
        this.currentJobKey = jec.getJobDetail().getKey();
        this.today = (String) dataMap.get("today");
        syncMesDataToDatabase();
    }

    private void syncMesDataToDatabase() {
        //先看紀錄幾筆了
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(currentPO, currentLineId);

        //確認已經開始了
        if (!l.isEmpty()) {
            //get PO quantity view 得到該工單所要做的機台數，超過Job self unsched.(台數 * 2 == 紀錄)
            //未到達台數持續Polling database find new data.
            List<PassStation> history = BasicService.getPassStationService().getPassStation(currentPO);
            List<PassStation> newData = (List<PassStation>) CollectionUtils.subtract(l, history);
            out.println("Job with " + currentJobKey + " begin insert data...");
            if (newData.isEmpty()) {
                out.println("Data No different");
            } else {
                out.println("Data insert " + (BasicService.getPassStationService().insertPassStation(newData) ? "Success" : "Fail"));
            }
            if (isPieceReachMaxium(l)) {
                jobSelfRemove();
            }
        }else{
            out.println("Data is empty");
        }
    }

    private boolean isPieceReachMaxium(List<PassStation> l) {
        int totalPiece = BasicService.getBabService().getPoTotalQuantity(currentPO);
        return (l.size() / 2) == totalPiece;
    }

    private void jobSelfRemove() {
        CronTrigMod ctm = CronTrigMod.getInstance();
        try {
            ctm.removeJob(currentJobKey);
        } catch (SchedulerException ex) {
            out.println(ex.toString());
        }
    }
}

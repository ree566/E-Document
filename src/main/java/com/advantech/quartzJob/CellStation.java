/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 此job的相關設定在CellScheduleJobServlet中，由此servlet分派子工作(本class)對webservice監聽並save data into database
 * 插入從WebService取得的過站資料job
 */
package com.advantech.quartzJob;

import com.advantech.entity.PassStation;
import com.advantech.service.PassStationService;
import com.advantech.webservice.WebServiceRV;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class CellStation extends QuartzJobBean {

    private String PO;
    private String type;
    private Integer apsLineId;
    
    @Autowired
    private PassStationService passStationService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        syncMesDataToDatabase();
    }

    //讓user自行開收工單，以免造成資料庫已關閉前端卻尚未關閉的情況
    private void syncMesDataToDatabase() {
        System.out.println("Begin check PO:" + PO + " / type:" + type);
        //先看紀錄幾筆了
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, type);

        //確認已經開始了
        if (!l.isEmpty()) {
            passStationService.checkDifferenceAndInsert(PO, type, apsLineId);
        } else {
            System.out.println("Data is empty.");
        }
    }

    

    public void setPO(String PO) {
        this.PO = PO;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setApsLineId(Integer apsLineId) {
        this.apsLineId = apsLineId;
    }

}

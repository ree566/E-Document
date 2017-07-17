/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 此job的相關設定在CellScheduleJobServlet中，由此servlet分派子工作(本class)對webservice監聽並save data into database
 * 插入從WebService取得的過站資料job
 */
package com.advantech.quartzJob;

import com.advantech.entity.PassStation;
import com.advantech.service.BasicService;
import com.advantech.webservice.WebServiceRV;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Wei.Cheng
 */
public class CellStation implements Job {

    private String PO;
    private String type;
    private Integer apsLineId;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        syncMesDataToDatabase();
    }

    //讓user自行開收工單，以免造成資料庫已關閉前端卻尚未關閉的情況
    private void syncMesDataToDatabase() {
        System.out.println("Begin check PO:" + PO + " / type:" + type);
        //先看紀錄幾筆了
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, type);

        //確認已經開始了
        if (!l.isEmpty()) {
            checkDifferenceAndInsert(PO, type, apsLineId);
        } else {
            System.out.println("Data is empty.");
        }
    }

    public static void checkDifferenceAndInsert(String PO, String type, Integer apsLineId) {

        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, type);
        List<PassStation> history = BasicService.getPassStationService().getPassStation(PO, type);
        List<PassStation> newData = (List<PassStation>) CollectionUtils.subtract(l, history);

        if (!newData.isEmpty()) {
            PassStation testData = newData.get(0);
            //Check data if matches current process apsLine or not.
            if (Objects.equals(testData.getLineId(), apsLineId)) {
                
                //Set type mark separate "Test, Pkg, Bab"
                for (PassStation p : newData) {
                    p.setType(type);
                }

                BasicService.getPassStationService().insertPassStation(newData);
            }
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

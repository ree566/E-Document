/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 此job的相關設定在CellScheduleJobServlet中，由此servlet分派子工作(本class)對webservice監聽並save data into database
 */
package com.advantech.quartzJob;

import com.advantech.entity.Cell;
import com.advantech.entity.PassStation;
import com.advantech.helper.CronTrigMod;
import com.advantech.service.BasicService;
import com.advantech.service.CellService;
import com.advantech.webservice.WebServiceRV;
import static java.lang.System.out;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
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

    private String PO;
    private Integer lineId;
    private Integer apsLineId;
    private JobKey currentJobKey;
    private TriggerKey currentTriggerKey;
    private String today;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        this.currentTriggerKey = jec.getTrigger().getKey();
        this.currentJobKey = jec.getJobDetail().getKey();
        syncMesDataToDatabase();
    }

    private void syncMesDataToDatabase() {
        //先看紀錄幾筆了
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, apsLineId);

        //確認已經開始了
        if (!l.isEmpty()) {
            checkDifferenceAndInsert(PO, apsLineId);
            
            //讓user自行開收工單，以免造成資料庫已關閉前端卻尚未關閉的情況
            //get PO quantity view 得到該工單所要做的機台數，超過Job self unsched.(台數 * 2 == 紀錄)
            //未到達台數持續Polling database find new data.
//            if (isPieceReachMaxium(l)) {
//                jobSelfRemove();
//            }
        }
    }

    public static void checkDifferenceAndInsert(String PO, int apsLineId) {

        out.println("Begin check");
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, apsLineId);
        List<PassStation> history = BasicService.getPassStationService().getPassStation(PO);
        List<PassStation> newData = (List<PassStation>) CollectionUtils.subtract(l, history);

        if (!newData.isEmpty()) {
            out.println("Begin insert");
            BasicService.getPassStationService().insertPassStation(newData);
        } else {
            out.println("No difference");
        }
    }

    private boolean isPieceReachMaxium(List<PassStation> l) {
        int totalPiece = BasicService.getBabService().getPoTotalQuantity(PO);
        return (l.size() / 2) == totalPiece;
    }

    private void jobSelfRemove() {
        try {
            CronTrigMod.getInstance().removeJob(currentJobKey);
            CellService cellService = BasicService.getCellService();
            List<Cell> list = cellService.getCellProcessing(lineId);
            if (!list.isEmpty()) {
                cellService.delete((Cell) list.get(0));
            }
        } catch (SchedulerException ex) {
            out.println(ex.toString());
        }
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public void setApsLineId(Integer apsLineId) {
        this.apsLineId = apsLineId;
    }

    public void setToday(String today) {
        this.today = today;
    }

}
